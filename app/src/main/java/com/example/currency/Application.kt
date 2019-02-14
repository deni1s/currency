package com.example.currency

import android.app.Application
import com.example.currency.modules.viewModule
import org.koin.android.ext.android.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(viewModule))
    }

}