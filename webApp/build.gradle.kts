import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer = KotlinWebpackConfig.DevServer().apply {
                    port = 3500
                    static(project.layout.projectDirectory.dir("src/wasmJsMain/resources").asFile.absolutePath, false)
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
        }
    }
}

tasks.register("runDev") {
    group = "application"
    description = "Sobe o dev server web com hot reload na porta 3500."
    dependsOn("wasmJsBrowserDevelopmentRun")
}

tasks.register("runProd") {
    group = "application"
    description = "Sobe o bundle de producao web na porta 3500."
    dependsOn("wasmJsBrowserProductionRun")
}