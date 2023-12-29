
buildscript {
    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.4.2")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
        //Firebase Crashlytics and Analytics
        classpath ("com.google.gms:google-services:4.4.0")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")

    }
}

plugins {
    id ("com.android.application") version "8.0.0" apply false
    id ("com.android.library") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
