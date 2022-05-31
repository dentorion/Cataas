plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        applicationId = "com.entin.cataas"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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

    Dependencies.base.apply {
        implementation(coreKtx)
        implementation(appcompat)
        implementation(material)
        implementation(constraintlayout)
        implementation(fragment)
    }

    // Hilt
    Dependencies.hilt.apply {
        implementation(mainHilt)
        kapt(compileAndroid)
    }

    // NAVIGATION COMPONENT
    Dependencies.navigation.apply {
        implementation(mainNavigation)
        implementation(ui)
    }

    // Lifecycle + ViewModel & LiveData
    Dependencies.lifecycle.apply {
        implementation(lifecycle)
        implementation(liveData)
        implementation(viewModel)
    }

    // Recyclerview
    Dependencies.recyclerview.apply {
        implementation(recyclerview)
    }

    // Glide
    Dependencies.glide.apply {
        implementation(glide)
    }

    // Splash screen
    Dependencies.splashScreen.apply {
        implementation(splashScreen)
    }

    // Retrofit + logging interceptor
    Dependencies.retrofit.apply {
        implementation(gson)
        implementation(retrofit)
        implementation(loggingInterceptor)
    }

    // Gson
    Dependencies.gson.apply {
        implementation(gson)
    }

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

kapt {
    correctErrorTypes = true
}