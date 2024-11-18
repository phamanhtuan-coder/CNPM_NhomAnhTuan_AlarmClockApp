plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"
}

android {
    namespace = "com.example.cnpm_nhomanhtuan_alarmclockapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cnpm_nhomanhtuan_alarmclockapp"
        minSdk = 26
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation ("androidx.startup:startup-runtime:1.1.1")
    implementation ("androidx.appcompat:appcompat:1.6.1") // Để sử dụng AppCompatActivity
    implementation ("androidx.activity:activity-compose:1.7.2") // Để sử dụng setContent với Compose
    implementation ("androidx.compose.material3:material3:1.1.0") // Để sử dụng Material3
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation ("androidx.work:work-runtime-ktx:2.8.0")
    implementation ("androidx.compose.foundation:foundation")
    implementation ("androidx.compose.material3:material3")
    implementation ("androidx.compose.runtime:runtime")
    implementation ("androidx.compose.ui:ui")
    implementation ("androidx.compose.material:material-icons-extended")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}