# SOAR Maps API Example

A production-ready Kotlin REST API for managing geographic map layers with PostGIS spatial queries, built with Micronaut framework.

## 🎯 Features

- 🗺️ **Spatial Queries**: Find map layers containing specific geographic points using PostGIS
- 🚀 **RESTful API**: Clean REST endpoints with proper HTTP semantics and versioning
- ✅ **Input Validation**: Comprehensive validation at all layers (controller, service, repository)
- 📊 **Database Support**: PostgreSQL with PostGIS extension for spatial operations
- 🔒 **Security**: Environment-based configuration, no hardcoded credentials
- 🧪 **Testing**: Complete unit and integration test coverage
- 📝 **Logging**: Structured logging with SLF4J and Logback
- 🐳 **Docker Support**: Multi-container deployment with Docker Compose
- 🏗️ **Clean Architecture**: Layered design (Controller → Service → Repository)
- 🔄 **DTOs**: Separate data transfer objects from domain entities

## 📚 Tech Stack

- **Language**: Kotlin 2.0
- **Framework**: Micronaut 4.5
- **Database**: PostgreSQL 16 with PostGIS 3.4
- **Build Tool**: Gradle 8.x (Kotlin DSL)
- **Testing**: JUnit 5, Mockito-Kotlin
- **Logging**: Logback with file rotation
- **Connection Pool**: HikariCP
- **Validation**: Jakarta Bean Validation

## 📋 Prerequisites

- **Java 21** or higher
- **Docker & Docker Compose** (for database)
- **Gradle** (included via wrapper)

## 🚀 Quick Start

### 1. Setup Environment
```cmd
copy .env.example .env
REM Edit .env with your database credentials
```

### 2. Start Database
```cmd
docker-compose up -d db
```

Wait for PostgreSQL to be ready. The `init.sql` script will automatically:
- Enable PostGIS extension
- Create `map_layers` table
- Add spatial indexes
- Insert sample data

### 3. Run Application
```cmd
gradlew.bat run
```

The API will start at: `http://localhost:8080`

### 4. Test API
```cmd
curl "http://localhost:8080/api/v1/map-layers/search?latitude=25.25&longitude=22.27"
```

## 📡 API Endpoints

### Search for Layers Containing a Point
```http
GET /api/v1/map-layers/search?latitude=25.25&longitude=22.27
```

**Query Parameters:**
- `latitude` (required): Latitude coordinate, range: -90 to 90
- `longitude` (required): Longitude coordinate, range: -180 to 180

**Response (200 OK):**
```json
{
  "query": {
    "latitude": 25.25,
    "longitude": 22.27
  },
  "results": [
    {
      "id": 1,
      "name": "North Africa Region",
      "geom": "POLYGON((20 20, 20 30, 30 30, 30 20, 20 20))"
    }
  ],
  "count": 1
}
```

### Get All Layers
```http
GET /api/v1/map-layers
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "North Africa Region",
    "geom": "POLYGON((20 20, 20 30, 30 30, 30 20, 20 20))"
  }
]
```

### Get Layer by ID
```http
GET /api/v1/map-layers/{id}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "North Africa Region",
  "geom": "POLYGON((20 20, 20 30, 30 30, 30 20, 20 20))"
}
```

**Response (404 Not Found):** When layer doesn't exist

### Create New Layer
```http
POST /api/v1/map-layers
Content-Type: application/json

{
  "name": "New Region",
  "geom": "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))"
}
```

**Response (201 Created):**
```json
{
  "id": 4,
  "name": "New Region",
  "geom": "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))"
}
```

### Error Responses
**400 Bad Request:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Latitude must be between -90 and 90",
  "path": "/api/v1/map-layers/search"
}
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── kotlin/com/example/
│   │   ├── Application.kt              # Main application entry point
│   │   ├── MapLayer.kt                 # Domain entity
│   │   ├── MapLayerDto.kt              # Data Transfer Objects
│   │   ├── MapLayerRepository.kt       # Database access layer
│   │   ├── MapLayerService.kt          # Business logic layer
│   │   ├── MapLayerController.kt       # REST API controller
│   │   └── ExceptionHandlers.kt        # Global error handling
│   └── resources/
│       ├── application.yml             # Application configuration
│       └── logback.xml                 # Logging configuration
└── test/
    ├── kotlin/
    │   ├── MapLayerServiceTest.kt      # Unit tests
    │   └── MapLayerControllerTest.kt   # Integration tests
    └── resources/
        ├── application-test.yml        # Test configuration
        └── logback-test.xml            # Test logging
```

## 🏛️ Architecture

The application follows a clean, layered architecture:

```
┌─────────────────────────────────────┐
│     Controller Layer                │  ← HTTP requests/responses
│  - MapLayerController                │     Input validation
│  - DTOs for API contracts           │     Error handling
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      Service Layer                  │  ← Business logic
│  - MapLayerService                   │     Coordinate validation
│  - Transaction boundaries           │     Logging
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│    Repository Layer                 │  ← Data access
│  - MapLayerRepository                │     SQL queries
│  - PostGIS spatial operations       │     CRUD operations
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   Database (PostgreSQL + PostGIS)   │  ← Data persistence
│  - map_layers table                  │     Spatial indexes
└─────────────────────────────────────┘
```

## 🧪 Testing

### Run All Tests
```cmd
gradlew.bat test
```

### View Test Report
```cmd
start build\reports\tests\test\index.html
```

### Test Coverage
- ✅ **Unit Tests** (`MapLayerServiceTest`): Business logic with mocked dependencies
- ✅ **Integration Tests** (`MapLayerControllerTest`): Full HTTP request/response cycle
- ✅ **Validation Tests**: Coordinate range validation
- ✅ **Error Handling Tests**: Exception scenarios

## ⚙️ Configuration

Configuration is managed through environment variables with sensible defaults.

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Application port | `8080` |
| `JDBC_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/soar` |
| `DB_USERNAME` | Database username | `your_username` |
| `DB_PASSWORD` | Database password | `your_password` |

### application.yml
```yaml
micronaut:
  application:
    name: soar-maps-api-example
  server:
    port: ${PORT:8080}
    cors:
      enabled: true

datasources:
  default:
    url: ${JDBC_URL:`jdbc:postgresql://localhost:5432/soar`}
    username: ${DB_USERNAME:`your_username`}
    password: ${DB_PASSWORD:`your_password`}
    maximum-pool-size: 10
```

## 🐳 Docker Deployment

### Start Full Stack
```cmd
docker-compose up -d
```

This starts:
- PostgreSQL with PostGIS extension
- Application container (port 8080)

### Stop Services
```cmd
docker-compose down
```

### View Logs
```cmd
docker-compose logs -f app
```

## 📊 Database Schema

```sql
CREATE TABLE map_layers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL
);

CREATE INDEX idx_map_layers_geom ON map_layers USING GIST (geom);
```

- **SRID 4326**: WGS 84 coordinate system (standard lat/lon)
- **GIST Index**: Optimizes spatial queries
- **Geometry Type**: Polygon

## 🔍 Spatial Queries

The application uses PostGIS functions for spatial operations:

```sql
-- Find layers containing a point
SELECT id, name, ST_AsText(geom) as geom 
FROM map_layers 
WHERE ST_Contains(
    geom, 
    ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)
);
```

## 📝 Logging

Logs are written to:
- **Console**: Development debugging
- **File**: `logs/application.log` with daily rotation

### Log Levels
- `com.example` → INFO
- `io.micronaut` → INFO
- `io.micronaut.data` → DEBUG (shows SQL queries)
- `io.netty` → WARN

## 🔧 Build Commands

```cmd
# Clean build
gradlew.bat clean build

# Compile only
gradlew.bat compileKotlin

# Run application
gradlew.bat run

# Run tests
gradlew.bat test

# Generate distribution
gradlew.bat distZip
```

## 🛠️ Development

### Adding Dependencies
Edit `build.gradle.kts`:
```kotlin
dependencies {
    implementation("group:artifact:version")
}
```

### Creating a New Endpoint
1. Add method to `MapLayerService`
2. Create DTO in `MapLayerDto.kt` (if needed)
3. Add controller method in `MapLayerController`
4. Write tests

## ✅ Best Practices Implemented

- ✅ **Separation of Concerns**: Clear layer boundaries
- ✅ **DTOs**: API contracts separate from entities
- ✅ **Input Validation**: Multiple validation layers
- ✅ **Error Handling**: Global exception handlers
- ✅ **Logging**: Structured with context
- ✅ **Testing**: Unit and integration tests
- ✅ **Documentation**: KDoc on all public APIs
- ✅ **Configuration**: Environment-based
- ✅ **Security**: No hardcoded secrets
- ✅ **Docker**: Container-ready deployment

## 🐛 Troubleshooting

### Tests Fail with kotlin-reflect Error
**Solution**: The `kotlin-reflect` dependency is required:
```kotlin
implementation("org.jetbrains.kotlin:kotlin-reflect")
```

### Database Connection Fails
**Solution**: Ensure PostgreSQL is running and credentials are correct:
```cmd
docker-compose up -d db
docker-compose logs db
```

### Port Already in Use
**Solution**: Change the port in `.env` or stop the conflicting service:
```
PORT=8081
```

## 📚 Additional Resources

- [Micronaut Documentation](https://docs.micronaut.io/)
- [PostGIS Documentation](https://postgis.net/documentation/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Micronaut Data](https://micronaut-projects.github.io/micronaut-data/latest/guide/)

## 📄 License

This is an example project for demonstration purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Write/update tests
5. Create a Pull Request

---

**Built with ❤️ using Kotlin and Micronaut**

