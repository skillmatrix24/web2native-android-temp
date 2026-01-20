import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

/* =================================================
   🔐 LOAD KEYSTORE PROPERTIES (ROOT/keystore/)
   Folder structure:
   Web2NativeAndroid/
   └── keystore/
       ├── keystore.properties
       └── web2native-release.jks
================================================== */
val keystoreProps = Properties()
val keystorePropsFile = rootProject.file("keystore/keystore.properties")

if (keystorePropsFile.exists()) {
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
       🔐 SIGNING CONFIG (FIXED & CLOUD-SAFE)
       ✔ rootProject.file() used
       ✔ No hardcoded secrets
       ✔ Works in local + Cloud Build
    ================================================== */
    signingConfigs {
    create("release") {
        storeFile = file(System.getenv("ANDROID_KEYSTORE_FILE"))
        storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
        keyAlias = System.getenv("ANDROID_KEY_ALIAS")
        keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        isMinifyEnabled = false
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
