import org.gradle.accessors.dm.LibrariesForLibs


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.epn.mockcrud.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.ktor.client.okhttp)
            
            // Room
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
            
            // ObjectBox
            implementation(libs.objectbox.android)
            implementation(libs.objectbox.kotlin)
            
            // DataStore & Security
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.security.crypto)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
