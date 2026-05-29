package com.itis.artistinfodagger

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itis.artistinfodagger.di.AppComponent
import com.itis.artistinfodagger.di.AppModule
import com.itis.artistinfodagger.di.DaggerAppComponent
import java.util.UUID

class ArtistInfoDaggerApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {

        super.onCreate()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        var userId = prefs.getString("user_id", null)
        if (userId == null) {
            userId = UUID.randomUUID().toString()
            prefs.edit().putString("user_id", userId).apply()
        }

        FirebaseCrashlytics.getInstance().setUserId(userId)
        FirebaseCrashlytics.getInstance().setCustomKey("user_id", userId)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}