plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("io.grpc:grpc-protobuf:1.62.2")
    implementation("io.grpc:grpc-netty:1.62.2")
    implementation("com.google.protobuf:protobuf-kotlin:3.25.3")
    
    // Sometimes needed by gRPC generated code for javax.annotation.Generated
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.62.2"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}
