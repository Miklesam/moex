import org.jetbrains.kotlin.config.JvmTarget

plugins {
    `kotlin-dsl`

}

group = "mikle.sam.moex.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        //jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.android.gradleApiPlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.android.tools.common)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.moex.android.application.compose.get().pluginId
            implementationClass = "mikle.sam.moex.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.moex.android.library.compose.get().pluginId
            implementationClass = "mikle.sam.moex.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.moex.android.library.asProvider().get().pluginId
            implementationClass = "mikle.sam.moex.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.moex.android.application.asProvider().get().pluginId
            implementationClass = "mikle.sam.moex.buildlogic.AndroidApplicationConventionPlugin"
        }
    }
}