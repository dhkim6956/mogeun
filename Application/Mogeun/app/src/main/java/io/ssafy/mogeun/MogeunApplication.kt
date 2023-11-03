package io.ssafy.mogeun

import android.app.Application
import io.ssafy.mogeun.data.AppContainer
import io.ssafy.mogeun.data.DefaultAppContainer

class MogeunApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}