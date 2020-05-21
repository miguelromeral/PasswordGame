package es.miguelromeral.password.classes

import android.app.Application
import timber.log.Timber


class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        isDebug = false
    }

    companion object {
        var isDebug = false
    }
}