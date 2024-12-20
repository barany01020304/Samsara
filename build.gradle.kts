// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.1" apply false
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false
    //room
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
}