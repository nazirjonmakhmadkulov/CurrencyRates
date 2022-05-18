package com.developer.valyutaapp.di

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.developer.valyutaapp.di.modules.*
import com.developer.valyutaapp.utils.SharedPreference
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CurrencyApp : Application() {
    private val prefs: SharedPreference by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CurrencyApp)
            androidLogger(Level.DEBUG)
            modules(
                listOf(
                    useCasesModule,
                    viewModelModule,
                    connectionInternet,
                    repositoryModule,
                    netModule,
                    apiModule,
                    databaseModule,
                    sharedPreference
                )
            )
        }
//        if (prefs.getTheme()) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}