plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.secrets)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.bruno13palhano.data"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    secrets {
        defaultPropertiesFileName = "local.defaults.properties"
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "com.bruno13palhano.data.CustomTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

tasks.getByName("preBuild").dependsOn("ktlintFormat")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
    testImplementation(libs.mockWebServer)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.coroutines.android)
    ksp(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.coroutines)
    implementation(libs.kotlinx.serialization.json)
}
