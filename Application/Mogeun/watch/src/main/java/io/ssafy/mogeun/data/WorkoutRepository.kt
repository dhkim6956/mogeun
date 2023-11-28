package io.ssafy.mogeun.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    val workoutDataStore: WorkoutDataStore
) {
    val activeWorkoutFlow: Flow<Boolean> = workoutDataStore.activeWorkoutFlow

    suspend fun setActiveWorkout(activeWorkout: Boolean) =
        workoutDataStore.setActiveWorkout(activeWorkout)

    val walkingPointsFlow: Flow<Int> = workoutDataStore.walkingPointsFlow

    suspend fun setWalkingPoints(points: Int) = workoutDataStore.setWalkingPoints(points)
}