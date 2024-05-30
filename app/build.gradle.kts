plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.example.homemanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.homemanagement"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/native-image/org.mongodb/bson/native-image.properties"
        }
    }
}



dependencies {

    //implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
   // implementation ("com.android.support:design:28.0.0")
    //implementation("com.android.support:appcompat-v7:28.0.0")
    //implementation("com.android.support:support-v4:28.0.0")
    implementation("androidx.room:room-runtime:2.4.0")
    implementation(libs.androidx.media3.common)
    kapt( "androidx.room:room-compiler:2.4.0")
    implementation("com.google.code.gson:gson:2.8.8")
    // Kotlin coroutine dependencies
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.androidx.constraintlayout)
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    // To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    kapt ("com.github.bumptech.glide:compiler:4.12.0")

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    val fragment_version = "1.6.2"

    // Java language implementation
    implementation("androidx.fragment:fragment:$fragment_version")
    // Kotlin
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    implementation ("nl.dionsegijn:konfetti-xml:2.0.4")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //implementation("com.android.support.constraint:constraint-layout:2.0.4.")

}



