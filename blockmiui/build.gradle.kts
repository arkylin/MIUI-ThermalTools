plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {

    signingConfigs {
        create("release") {
        }
    }
    compileSdk = 31
    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
    namespace = "cn.fkj233.miui"
}

dependencies {
    implementation("androidx.annotation:annotation:1.3.0")
}