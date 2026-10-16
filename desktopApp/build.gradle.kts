import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinJvm)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.shared)
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(libs.koin.core)
}

compose.desktop {
    application {
        mainClass = "br.com.semesperanca.frontendleads.MainKt"

        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "frontend-leads"
            packageVersion = "0.1.0"
        }

        buildTypes {
            release {
                proguard {
                    isEnabled.set(false)
                }
            }
        }
    }
}