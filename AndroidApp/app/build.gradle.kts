plugins {

    id("com.google.gms.google-services")

    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.iot_esp32_android_firebase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.iot_esp32_android_firebase"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Các thư viện cần thiết
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database) // Đảm bảo Firebase Database được thêm đúng

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase Analytics (có thể loại bỏ nếu không cần thiết)
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")

    // Các thư viện cho testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.google.firebase:firebase-database:21.0.0")


}
