# Soar Maps API - Project Summary

## Overview
This is a Micronaut-based REST API application that finds map layers containing a given geographical point using PostGIS spatial queries.

## Technology Stack
- **Framework**: Micronaut 4.5.0
- **Language**: Kotlin 1.9.24
- **Data Layer**: Micronaut Data JDBC
- **Database**: PostgreSQL with PostGIS extension
- **Build Tool**: Gradle 8.13

## Project Structure
```
soar-maps-api-example/
├── src/main/kotlin/com/example/
│   ├── Application.kt          # Main application entry point
│   ├── MapLayer.kt             # Entity representing a map layer
│   ├── MapLayerRepository.kt   # Data repository with spatial queries
│   └── MapLayerController.kt   # REST API controller
├── src/main/resources/
│   └── application.yml         # Application configuration
├── build.gradle.kts            # Gradle build configuration
├── docker-compose.yml          # PostgreSQL + PostGIS container
├── init.sql                    # Database initialization script
└── README.md                   # User documentation
```

## Key Components

### 1. MapLayer Entity
- Represents a geographic layer with an ID, name, and geometry
- Geometry is stored as WKT (Well-Known Text) for simplicity
- Maps to the `map_layers` table in PostgreSQL

### 2. MapLayerRepository
- Uses Micronaut Data JDBC
- Custom query using PostGIS functions:
  - `ST_Contains()` - Checks if a geometry contains a point
  - `ST_MakePoint()` - Creates a point from longitude/latitude
  - `ST_SetSRID()` - Sets the spatial reference system (SRID 4326 = WGS84)
  - `ST_AsText()` - Converts geometry to WKT format

### 3. MapLayerController
- REST endpoint: `GET /map-layers/search?latitude={lat}&longitude={lng}`
- Returns a list of map layers containing the specified point

## API Usage

### Endpoint
```
GET /map-layers/search?latitude={latitude}&longitude={longitude}
```

### Example Request
```bash
curl "http://localhost:8080/map-layers/search?latitude=25.25306481&longitude=22.27009466"
```

### Example Response
```json
[
  {
    "id": 1,
    "name": "North Africa Region",
    "geom": "POLYGON((20 20,20 30,30 30,30 20,20 20))"
  },
  {
    "id": 2,
    "name": "Sahara Desert Section",
    "geom": "POLYGON((22 22,22 28,28 28,28 22,22 22))"
  }
]
```

## Database Setup

### Using Docker Compose
1. Start PostgreSQL with PostGIS:
   ```bash
   docker-compose up -d
   ```

2. Initialize the database:
   ```bash
   # Connect to the database
   psql -h localhost -U your_username -d soar
   
   # Run the init script
   \i init.sql
   ```

### Database Schema
```sql
CREATE TABLE map_layers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL
);

CREATE INDEX idx_map_layers_geom ON map_layers USING GIST (geom);
```

## Building and Running

### Build the application
```bash
./gradlew build
```

### Run the application
```bash
./gradlew run
```

The application will start on `http://localhost:8080`

## Configuration

### Database Connection (application.yml)
```yaml
datasources:
  default:
    url: jdbc:postgresql://localhost:5432/soar
    driverClassName: org.postgresql.Driver
    username: your_username
    password: "your_password"
```

## PostGIS Spatial Reference System
- **SRID 4326**: WGS84 coordinate system (standard latitude/longitude)
- Latitude range: -90 to 90 (South to North)
- Longitude range: -180 to 180 (West to East)

## Future Enhancements
1. Add support for different geometry types (Point, LineString, MultiPolygon)
2. Implement pagination for large result sets
3. Add filtering by layer properties
4. Support for buffer/distance queries
5. GeoJSON response format option
6. Full PostGIS PGgeometry type support with custom type converters

## Troubleshooting

### Build Issues
- Ensure Java 21 is installed
- Run `./gradlew clean build` to clear any cached artifacts

### Database Connection Issues
- Verify PostgreSQL is running: `docker ps`
- Check connection details in `application.yml`
- Ensure PostGIS extension is enabled: `CREATE EXTENSION IF NOT EXISTS postgis;`

### No Results from API
- Verify data exists in the database
- Check that coordinates are in the correct order (longitude, latitude for PostGIS functions)
- Ensure SRID matches (4326)

