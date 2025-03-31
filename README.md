# SWIFT-task

A Spring Boot application for parsing, storing, and managing SWIFT data in a MongoDB database. The app exposes a REST API to support fast CRUD operations

## Features

- **SWIFT File Parsing** – Parses raw SWIFT data from a provided input file.
- **MongoDB Integration** – Efficient document storage with Spring Data MongoDB.
- **Bulk Operations** – Uses MongoDB bulk inserts for efficiently storing multiple history records in one operation.
- **Fast Lookup** – Optimized querying for swiftCode thanks to cluster-index.
- **Full REST API** – CRUD endpoints for managing SWIFT records.
- **Dockerized** – Easy deployment using Docker and Docker Compose.


## Tech Stack

- **Java & Spring Boot**
- **Spring Data MongoDB**
- **Gradle Kotlin DSL**
- **Docker + Docker Compose**
- **JUnit for testing**

## Getting Started

### Prerequisites
- Docker + Docker Compose
- Java 17 +
- Git

### Running with Docker (recommended)

```bash
git clone https://github.com/Michaaaal/SWIFT-task.git
cd SWIFT-task
docker-compose up --build
```

App will be available at: [http://localhost:8080](http://localhost:8080)

### Running Locally (without Docker)

1. Make sure MongoDB is running locally on port `27017`.
2. Run the app in IDE or (.\):
```bash
./gradlew bootRun
```
3. Run tests:
```bash
./gradlew test
```

### Postman Workspace
import this file for convinient endpoint testing
[swift-postman.json](https://github.com/user-attachments/files/19529489/swift-postman.json)


