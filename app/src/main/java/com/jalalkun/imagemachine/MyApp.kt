package com.jalalkun.imagemachine

import android.app.Application
import com.jalalkun.imagemachine.di.machineDataDaoModule
import com.jalalkun.imagemachine.di.machineDataRepoModule
import com.jalalkun.imagemachine.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApp)
            modules(
                machineDataDaoModule,
                machineDataRepoModule,
                viewModelModule
            )
        }
    }
}