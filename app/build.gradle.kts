import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Leer las propiedades locales
val propiedadesLocales = Properties()
val archivoLocal = rootProject.file("local.properties")

if (archivoLocal.exists()) {
    archivoLocal.inputStream().use {
        propiedadesLocales.load(it)
    }
}

val appToken = propiedadesLocales.getProperty("DATOS_APP_TOKEN") ?: ""

android {
    namespace = "co.edu.unal.directorioeducativo"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "co.edu.unal.directorioeducativo"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "DATOS_APP_TOKEN",
            "\"$appToken\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
