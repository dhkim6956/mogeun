package io.ssafy.mogeun.data

import android.content.Context

interface AppContainer {
    val dataLayerRepository: DataLayerRepository
    val workoutDataStore: WorkoutDataStore
    val workoutRepository: WorkoutRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {
    override val dataLayerRepository: DataLayerRepository by lazy {
        AndroidDataLayerRepository(context)
    }

    override val workoutDataStore: WorkoutDataStore by lazy {
        WorkoutDataStore(context)
    }

    override val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepository(workoutDataStore)
    }
}