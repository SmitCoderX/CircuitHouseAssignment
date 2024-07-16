plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.smitcoderx.circuithouseassignment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smitcoderx.circuithouseassignment"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)

    // Fragment
    implementation(libs.androidx.fragment)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // OkHttp
    implementation(libs.okHttpClient)

    // Navigation
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigation.ui)

    // Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    // RecyclerView
    implementation(libs.recyclerView)

    // ViewModel + LiveData
    implementation(libs.viewModel)
    implementation(libs.liveData)
    kapt(libs.lifecycle.compiler)



}

kapt {
    correctErrorTypes = true
}