/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package io.ssafy.mogeun

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.Wearable
import io.ssafy.mogeun.data.ForegroundOnlyWorkoutService
import io.ssafy.mogeun.ui.theme.MogeunTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val vibrator: Vibrator by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(Vibrator::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private var activeWalkingWorkout = false
        set(newActiveStatus) {
            if (field != newActiveStatus) {
                field = newActiveStatus
                if (newActiveStatus) {

                } else {

                }
                updateOutput(walkingPoints)
            }
        }

    private var walkingPoints = 0

    // The remaining variables are related to the binding/monitoring/interacting with the
    // service that gathers all the data to calculate walking points.
    private var foregroundOnlyServiceBound = false

    // Gathers mock location/sensor data for walking workouts and also promotes itself to a
    // foreground service with Ongoing Notification to continue gathering data when a user is
    // engaged in an active walking workout.
    private var foregroundOnlyWalkingWorkoutService: ForegroundOnlyWorkoutService? = null

    // Monitors connection to the service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyWorkoutService.LocalBinder
            foregroundOnlyWalkingWorkoutService = binder.walkingWorkoutService
            foregroundOnlyServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyWalkingWorkoutService = null
            foregroundOnlyServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp(mainViewModel.execName.value, mainViewModel.timerString.value, mainViewModel.messageString.value, mainViewModel::startSet, mainViewModel::stopSet, mainViewModel::clearMessage, mainViewModel.progress.value)
        }

        val setObserver = Observer<Boolean> { setEnded ->
            if(setEnded) {
                vibrate()
                mainViewModel.resetSetEnded()
            }
        }
        val messageObserver = Observer<Boolean> { setEnded ->
            if(setEnded) {
                vibrate(500)
                mainViewModel.resetMessageReceived()
            }
        }

        mainViewModel.setEnded.observe(this, setObserver)
        mainViewModel.messageReceived.observe(this, messageObserver)
    }

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, ForegroundOnlyWorkoutService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (foregroundOnlyServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyServiceBound = false
        }
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        messageClient.addListener(mainViewModel)
    }

    override fun onPause() {
        super.onPause()
        messageClient.removeListener(mainViewModel)
    }

    fun onClickWorkout(view: View) {
        Log.d("workout", "onClickWalkingWorkout()")
        if (activeWalkingWorkout) {
            foregroundOnlyWalkingWorkoutService?.stopWalkingWorkout()
        } else {
            foregroundOnlyWalkingWorkoutService?.startWalkingWorkout()
        }
    }

    private fun updateOutput(points: Int) {
        Log.d("workout", "updateOutput()")
        val output = "포인트 : $points"
    }

    private fun vibrate(time: Long = 1000) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(time)
        }
    }
}