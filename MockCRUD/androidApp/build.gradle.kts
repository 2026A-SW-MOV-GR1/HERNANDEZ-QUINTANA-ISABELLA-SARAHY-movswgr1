plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.objectbox)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.compose)
    
    // Usamos el BOM de Compose para asegurar consistencia en versiones de Google
    implementation(platform(libs.compose.bom))
    
    // Usamos las librerías de Compose Multiplatform/Material3
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
    
    // Navegación
    implementation(libs.androidx.navigation.compose)
    
    // Iconos adicionales
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Coil
    implementation(libs.coil.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // ObjectBox
    implementation(libs.objectbox.android)
    implementation(libs.objectbox.kotlin)
    ksp(libs.objectbox.processor)

    // DataStore & Security
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodelCompose)
}

android {
    namespace = "com.epn.mockcrud"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.epn.mockcrud"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
