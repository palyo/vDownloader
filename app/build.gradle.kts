plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.video.downloader"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.video.downloader"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    viewBinding.isEnabled = true

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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.sdp.android)
    implementation(libs.preference.ktx)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.jsoup)
    implementation(libs.kotlin.stdlib)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.glide)
    implementation(libs.jp.wasabeef.recyclerview.animators)
    implementation(libs.jp.wasabeef.glide.transformations)
    implementation("io.github.udhayarajan:VidSnapKit:5.8.0")
}