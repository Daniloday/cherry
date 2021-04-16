package com.jevely.cherry

import android.app.Application
import com.jevely.cherry.di.appModule
import com.jevely.cherry.di.dataSourceModule
import com.onesignal.OneSignal
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
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

    companion object {
        const val AF_DEV_KEY = "2KTn2FFy4YwGYoQEgmsDCg"
        const val BASE_URL = "https://herlionse.space/2ZDW2p"
        const val ORGANIC_URL = "https://herlionse.space/2ZDW2p"
        const val URL_REDIRECT = "github.com"
        const val PACKAGE = "com.jevely.cherry"
        private const val ONESIGNAL_APP_ID = "d878f387-dc9b-48b8-9e30-86acb0c1d496"
    }
}