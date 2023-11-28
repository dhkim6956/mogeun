package io.ssafy.mogeun.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import io.ssafy.mogeun.MainActivity
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundOnlyWorkoutService: LifecycleService() {
    private val workoutsRepository: WorkoutRepository by lazy {
        (application as MogeunApplication).container.workoutRepository
    }

    private lateinit var notificationManager: NotificationManager

    private var configurationChange = false

    private var serviceRunningInForeground = false

    private val localBinder = LocalBinder()

    private var walkingWorkoutActive = false

    private var mockDataForWorkoutJob: Job? = null

    private fun setActiveWorkout(active: Boolean) = lifecycleScope.launch {
        workoutsRepository.setActiveWorkout(active)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")

        workoutsRepository.activeWorkoutFlow.asLiveData().observe(this) {
                active ->
            if (walkingWorkoutActive != active) {
                walkingWorkoutActive = active
            }
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand()")

        val cancelWorkoutFromNotification =
            intent?.getBooleanExtra(EXTRA_CANCEL_WORKOUT_FROM_NOTIFICATION, false)
                ?: false

        if (cancelWorkoutFromNotification) {
            stopWorkoutWithServiceShutdownOption(stopService = true)
        }
        // Tells the system not to recreate the service after it's been killed.
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d(TAG, "onBind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // move itself back to a background services.
        notForegroundService()
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // move itself back to a background services.
        notForegroundService()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")

        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to maintain the 'while-in-use' label (usually needed for location services).
        // NOTE: If this method is called due to a configuration change in MainActivity,
        // we do nothing.
        if (!configurationChange && walkingWorkoutActive) {
            Log.d(TAG, "Start foreground service")
            val notification =
                generateNotification("알람시작")
            // startForeground takes care of notificationManager.notify(...).
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        // Ensures onRebind() is called if MainActivity (client) rebinds.
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    private fun notForegroundService() {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
    }

    fun startWalkingWorkout() {
        Log.d(TAG, "startWalkingWorkout()")

        setActiveWorkout(true)

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, ForegroundOnlyWorkoutService::class.java))

        // Normally, you would subscribe to location and sensor callbacks here, but since this
        // is a simplified example to teach Ongoing Activities, we are mocking the data.
        mockDataForWorkoutJob = lifecycleScope.launch {
            mockSensorAndLocationForWorkout()
        }
    }

    fun stopWalkingWorkout() {
        Log.d(TAG, "stopWalkingWorkout()")
        stopWorkoutWithServiceShutdownOption(false)
    }

    private fun stopWorkoutWithServiceShutdownOption(stopService: Boolean) {
        Log.d(TAG, "stopWalkingWorkout()")
        mockDataForWorkoutJob?.cancel()

        lifecycleScope.launch {
            val job: Job = setActiveWorkout(false)
            if (stopService) {
                // Waits until DataStore data is saved before shutting down service.
                job.join()
                stopSelf()
            }
        }
    }

    // Normally, you would listen to the location and sensor data and calculate your points with
    // an algorithm, but we are mocking the data to simply this so we can focus on learning about
    // the Ongoing Activity API.
    private suspend fun mockSensorAndLocationForWorkout() {
        for (walkingPoints in 0 until 100) {
            if (serviceRunningInForeground) {
                val notification = generateNotification(
                    "포인트"
                )
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
            Log.d(TAG, "mockSensorAndLocationForWalkingWorkout(): $walkingPoints")
            workoutsRepository.setWalkingPoints(walkingPoints) //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            delay(THREE_SECONDS_MILLISECONDS)
        }
    }

    private fun generateNotification(mainText: String): Notification {
        Log.d(TAG, "generateNotification()")

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data (note, the main notification text comes from the parameter above).
        val titleText = "알림 제목"

        // 1. Create Notification Channel.
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT)

        // Adds NotificationChannel to system. Attempting to create an
        // existing notification channel with its original values performs
        // no operation, so it's safe to perform the below sequence.
        notificationManager.createNotificationChannel(notificationChannel)

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainText)
            .setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent(this, MainActivity::class.java)

        val cancelIntent = Intent(this, ForegroundOnlyWorkoutService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_WORKOUT_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // 4. Build and issue the notification.
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        // TODO: Review Notification builder code.
        val notificationBuilder = notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            // Makes Notification an Ongoing Notification (a Notification with a background task).
            .setOngoing(true)
            // For an Ongoing Activity, used to decide priority on the watch face.
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.dumbbell_mid,
                "실행",
                activityPendingIntent
            )
            .addAction(
                R.drawable.dumbbell_mid,
                "정지",
                servicePendingIntent
            )

        // TODO: Create an Ongoing Activity.
        val ongoingActivityStatus = Status.Builder()
            // Sets the text used across various surfaces.
            .addTemplate(mainText)
            .build()

        val ongoingActivity =
            OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, notificationBuilder)
                // Sets icon that will appear on the watch face in active mode. If it isn't set,
                // the watch face will use the static icon in active mode.
                .setAnimatedIcon(R.drawable.dumbbell_animated)
                // Sets the icon that will appear on the watch face in ambient mode.
                // Falls back to Notification's smallIcon if not set. If neither is set,
                // an Exception is thrown.
                .setStaticIcon(R.drawable.dumbbell_mid)
                // Sets the tap/touch event, so users can re-enter your app from the
                // other surfaces.
                // Falls back to Notification's contentIntent if not set. If neither is set,
                // an Exception is thrown.
                .setTouchIntent(activityPendingIntent)
                // In our case, sets the text used for the Ongoing Activity (more options are
                // available for timers and stop watches).
                .setStatus(ongoingActivityStatus)
                .build()

        // Applies any Ongoing Activity updates to the notification builder.
        // This method should always be called right before you build your notification,
        // since an Ongoing Activity doesn't hold references to the context.
        ongoingActivity.apply(applicationContext)

        return notificationBuilder.build()
    }

    inner class LocalBinder : Binder() {
        internal val walkingWorkoutService: ForegroundOnlyWorkoutService
            get() = this@ForegroundOnlyWorkoutService
    }

    companion object {
        private const val TAG = "ForegroundOnlyService"

        private const val THREE_SECONDS_MILLISECONDS = 3000L

        private const val PACKAGE_NAME = "io.ssafy.mogeun"

        private const val EXTRA_CANCEL_WORKOUT_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_SUBSCRIPTION_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "workout_channel_01"
    }
}