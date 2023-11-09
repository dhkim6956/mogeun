package io.ssafy.mogeun.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDetail(val detail: Exercise) : Parcelable

class ExerciseDetailType : NavType<ExerciseDetail>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ExerciseDetail? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): ExerciseDetail {
        return Gson().fromJson(value, ExerciseDetail::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ExerciseDetail) {
        bundle.putParcelable(key, value)
    }
}