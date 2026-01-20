import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

/* =================================================
   🔐 KEYSTORE LOADING STRATEGY (SMART)
   Priority:
   1️⃣ ENV variables (Cloud Build / CI)
   2️⃣ keystore/keystore.properties (Local build)
================================================== */

val keystoreProps = Properties()
val keystorePropsFile = rootProject.file("keystore/keystore.properties")

val isCI = System.getenv("ANDROID_KEYSTORE_FILE") != null

if (!isCI && keystorePropsFile.exists()) {
    keystoreProps.load(FileInputStream(keystorePropsFile))
}

android {

    namespace = "com.web2native.template"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.web2native.template"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    /* =================================================
       🔐 SIGNING CONFIG (LOCAL + CLOUD SAFE)
    ================================================== */
    signingConfigs {

        create("release") {

            if (isCI) {
                // ☁️ Cloud Build / CI
                storeFile = file(System.getenv("ANDROID_KEYSTORE_FILE"))
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")

            } else if (keystoreProps.isNotEmpty()) {
                // 💻 Local build
                storeFile = rootProject.file(
                    "keystore/${keystoreProps["storeFile"]}"
                )
                storePassword = keystoreProps["storePassword"] as String
                keyAlias = keystoreProps["keyAlias"] as String
                keyPassword = keystoreProps["keyPassword"] as String
            }
        }
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            // default debug keystore
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
