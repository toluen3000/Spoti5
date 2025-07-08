import java.util.Properties

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Apply the Google Services plugin
    id ("com.google.gms.google-services")
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.dagger.hilt.android")
    id ("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.spoti5"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spoti5"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // load fields from local.properties file
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${properties["SPOTIFY_CLIENT_ID"]}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${properties["SPOTIFY_CLIENT_SECRET"]}\"")
        buildConfigField("String", "SPOTIFY_REDIRECT_URI", "\"${properties["SPOTIFY_REDIRECT_URI"]}\"")
        // for call using the Spotify API
        // authentication is required BuildConfig.CLIENT_ID and BuildConfig.CLIENT_SECRET

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Spotify App Remote SDK
    implementation(files("../spotify-app-remote-release-0.8.0.aar"))
    // Note: Ensure the Spotify App Remote SDK is correctly placed in the project structure

    // to authenticate and interact with Spotify.
    implementation("com.spotify.android:auth:1.2.3")


    // Lifecycle (ViewModel, LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Updated for Kotlin 2.0.x
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.56.1")
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    // Retrofit + Gson + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0") // Updated for Kotlin 2.0.x
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Moshi Converter for Retrofit
    implementation("com.squareup.retrofit2:converter-moshi:2.5.0")

    // Room (Local DB)
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Glide (Image Loading)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")

    // Lottie (Animation)
    implementation("com.airbnb.android:lottie:6.4.0")

    implementation("com.tbuonomo:dotsindicator:5.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Firebase (FCM)
    implementation("com.google.firebase:firebase-messaging:23.4.1")

    implementation ("com.yazantarifi:slider:1.0.0")

    //Swipe Refresh Layout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // Firebase Push Notifications
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics")


    // Maps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    //exoplayer
    implementation("androidx.media3:media3-exoplayer:1.7.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.7.1")
    implementation("androidx.media3:media3-ui:1.7.1")
    // for PlayerNotificationManager

    // browser
    implementation("androidx.browser:browser:1.8.0")

    //splash screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    // round image view
    implementation("de.hdodenhof:circleimageview:3.1.0")


}