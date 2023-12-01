package com.example.currency.converter

import android.app.Application
import com.example.currency.converter.di.connectedModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger() // Log Koin into Android logger
            androidContext(this@App) // Reference Android context

            // Load modules
            modules(connectedModules)
        }
    }
}