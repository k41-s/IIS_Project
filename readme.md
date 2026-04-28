# Interoperability of Information Systems (IIS) Project

This repository contains a fully implemented distributed system demonstrating interoperability across various modern architectural styles and protocols. Built as a comprehensive course project, it showcases seamless communication between microservices and a desktop client using REST, SOAP, GraphQL, and gRPC.

## Tech Stack

### Backend (Core & Weather Services)
* **Language/Framework:** Java, Spring Boot
* **Security:** Spring Security, JWT (Access & Refresh Tokens)
* **Protocols:** REST, GraphQL (Spring for GraphQL), SOAP (Spring Web Services), gRPC (Netty)
* **Validation:** JSON Schema (NetworkNT), Jakarta XML Validation (XSD)
* **Build Tool:** Maven

### Frontend (Desktop Client)
* **Language/Framework:** Kotlin, Compose Multiplatform (Desktop JVM)
* **Networking:** Ktor Client, Apollo GraphQL Client
* **Build Tool:** Gradle

## Key Features

* **Role-Based Desktop Client:** A beautiful Compose Multiplatform app that consumes all APIs. Features UI and backend-enforced role restrictions (Admin for full access, User for read-only access).
* **Dual REST API:** A toggleable API mode that allows switching between a public dummy API and a secure, custom-built REST API protected by JWT authentication.
* **Strict Payload Validation:** Dedicated endpoints that validate incoming JSON and XML payloads against strict schemas (JSON Schema and XSD) before persisting data.
* **SOAP & XPath Pipeline:** A SOAP endpoint that triggers a database export to an XML file, performs Jakarta XSD validation on the generated file, and returns search results using dynamic XPath queries.
* **GraphQL Integration:** A complete GraphQL API mapping to the core backend services, utilizing the same underlying database and security context.
* **gRPC Weather Microservice:** A standalone gRPC server that fetches, parses, and streams live XML weather data from DHMZ (Croatian Meteorological Service), supporting partial city name matching.

## Project Structure

The project is split into three main modules:
* `IIS-core-backend`: The primary Spring Boot application handling REST, SOAP, GraphQL, JWT Auth, and database operations.
* `IIS-weather-service`: A lightweight gRPC microservice for fetching real-time weather data.
* `IIS_client_app`: The Kotlin Compose Multiplatform desktop application.

## How to Use & Run (IntelliJ IDEA)

Because this project relies heavily on gRPC and Protobuf, you must generate the stub files for both the backend and frontend before running the services. The easiest way to do this is using IntelliJ's built-in Maven and Gradle tool windows.

### 1. Start the Weather Service (gRPC)
Open the `IIS-weather-service` folder as a project in IntelliJ IDEA.

**Generate Protobuf Stubs:** 1. Open the Maven tool window (usually on the right side of the screen).
2. Expand `IIS-weather-service` > `Plugins` > `protobuf`.
3. Double-click `protobuf:compile`, wait for it to finish, then double-click `protobuf:compile-custom`.
*(Alternatively, just double-click `Lifecycle` > `compile` which usually triggers the generation).*

**Run the Server:**
1. Open `src/main/java/.../IisWeatherServiceApplication.java`.
2. Click the green Play button next to the public class or main method.
   *The gRPC server will start on port 9091.*

### 2. Start the Core Backend
Open the `IIS-core-backend` folder as a project in IntelliJ IDEA.

**Compile the Project:**
1. Open the Maven tool window.
2. Expand `IIS-core-backend` > `Lifecycle` and double-click `compile` to ensure all JAXB classes and dependencies are generated.

**Run the Server:**
1. Open `src/main/java/.../IisCoreBackendApplication.java`.
2. Click the green Play button next to the main method.
   *The core backend will start on port 8080.*

### 3. Start the Desktop Client
Open the `IIS_client_app` folder as a project in IntelliJ IDEA. Let the initial Gradle sync finish completely.

**Generate Kotlin Protobuf Stubs:**
1. Open the Gradle tool window (on the right side of the screen).
2. Expand `IIS_client_app` > `grpc-api` > `Tasks` > `build`.
3. Double-click `generateProto` (or double-click `build` if `generateProto` is not visible).

**Run the Desktop Application:**
1. Navigate to `composeApp/src/jvmMain/kotlin/hr/algebra/iis_client_app/main.kt`.
2. Click the green Play button next to `fun main()`.

## Authentication & Access Control

To interact with the custom REST API, GraphQL, and SOAP endpoints within the desktop client, you must be authenticated.

* **Account Registration:** You can register a new account directly from the desktop client's authentication screen.
* **Default Permissions:** For security purposes, any newly registered account is automatically assigned a standard **User** role. This is a **Read-Only** account, meaning you will only be able to perform `GET` requests across the APIs.
* **Admin Access:** To test full system functionality (including `POST`, `PUT`, and `DELETE` operations), an existing database administrator must manually update your account's role to **Admin** within the backend database.