import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
       🔐 SIGNING CONFIG (BULLETPROOF)
       - storeFile ALWAYS set
       - Secrets only for passwords
    ================================================== */
    signingConfigs {

        create("release") {

            // ✅ ALWAYS SET (CI + Local both)
            storeFile = rootProject.file("keystore/web2native-release.jks")

            // 🔐 Passwords / alias
            val storePwd = System.getenv("KEYSTORE_PASSWORD")
            val alias = System.getenv("KEY_ALIAS")
            val keyPwd = System.getenv("KEY_PASSWORD")

            if (storePwd != null && alias != null && keyPwd != null) {
                // ☁️ Cloud Build
                storePassword = storePwd
                keyAlias = alias
                keyPassword = keyPwd
            } else {
                // 💻 Local fallback
                val propsFile = rootProject.file("keystore/keystore.properties")
                if (propsFile.exists()) {
                    val props = Properties()
                    props.load(FileInputStream(propsFile))
                    storePassword = props["storePassword"] as String
                    keyAlias = props["keyAlias"] as String
                    keyPassword = props["keyPassword"] as String
                }
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
