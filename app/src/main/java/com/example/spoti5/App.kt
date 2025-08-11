package com.example.spoti5

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.spoti5.utils.Manager.SettingsManager
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate() {
        super.onCreate()
//        applyTheme()
    }

    fun applyTheme() {
        val isDarkMode = settingsManager.isDarkMode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}