package io.ssafy.mogeun.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutDataStore(private val context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        name = WORKOUTS_DATASTORE_NAME
    )

    val activeWorkoutFlow: Flow<Boolean> = context.datastore.data.map {
        it[ACTIVE_WORKOUT_KEY] ?: false
    }

    suspend fun setActiveWorkout(activeWorkout: Boolean) {
        context.datastore.edit {
            it[ACTIVE_WORKOUT_KEY] = activeWorkout
        }
    }

    val walkingPointsFlow: Flow<Int> = context.datastore.data.map {
        it[WALKING_POINTS_KEY] ?: 0
    }

    suspend fun setWalkingPoints(walkingPoints: Int) {
        context.datastore.edit {
            it[WALKING_POINTS_KEY] = walkingPoints
        }
    }

    companion object {
        private const val WORKOUTS_DATASTORE_NAME = "workouts_datastore"

        private val ACTIVE_WORKOUT_KEY = booleanPreferencesKey("active_workout")
        private val WALKING_POINTS_KEY = intPreferencesKey("walking_points")
    }

}