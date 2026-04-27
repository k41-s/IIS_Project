import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0"
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Ktor HTTP Client
            implementation("io.ktor:ktor-client-core:2.3.9")
            implementation("io.ktor:ktor-client-cio:2.3.9")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.9")
            implementation("io.ktor:ktor-client-auth:2.3.9")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.9")
            
            // JSON Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            
            // Depends on our new pure JVM module containing the gRPC client
            implementation(project(":grpc-api"))
            
            // Expose gRPC and Protobuf classes to the UI directly
            implementation("io.grpc:grpc-kotlin-stub:1.4.1")
            implementation("io.grpc:grpc-protobuf:1.62.2")
            implementation("io.grpc:grpc-netty:1.62.2")
            implementation("com.google.protobuf:protobuf-kotlin:3.25.3")
        }
    }
}

compose.desktop {
    application {
        mainClass = "hr.algebra.iis_client_app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "hr.algebra.iis_client_app"
            packageVersion = "1.0.0"
        }
    }
}
