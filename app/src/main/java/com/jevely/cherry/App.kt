package com.jevely.cherry

import android.app.Application
import com.jevely.cherry.di.appModule
import com.jevely.cherry.di.dataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModule, dataSourceModule)
        }
    }
}