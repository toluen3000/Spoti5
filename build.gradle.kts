// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.navigation) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.parcelize) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.9.1"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}