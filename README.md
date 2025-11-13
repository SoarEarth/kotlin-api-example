# Soar Maps API Example

This is a simple Micronaut API application that demonstrates how to find map layers containing a given point using PostGIS spatial queries.

## Quick Start

1. **Start the database:**
   ```bash
   docker-compose up -d
   ```

2. **Initialize the database with sample data:**
   Connect to the database and run the `init.sql` script:
   ```bash
   psql -h localhost -U your_username -d soar -f init.sql
   ```
   
   Or manually create the tables (see Database Setup section below).

3. **Run the application:**
   ```bash
   ./gradlew run
   ```

4. **Test the API:**
   ```bash
   curl "http://localhost:8080/map-layers/search?latitude=25.25306481&longitude=22.27009466"
   ```
   
   Or run the test script:
   ```bash
   test-api.bat
   ```

## Prerequisites

- Java 21 or higher
- Gradle 8.5 or higher
- Docker and Docker Compose (for running a local PostgreSQL database)

## Database Setup

The application uses a PostgreSQL database with the PostGIS extension. You can use the provided `docker-compose.yml` file to start a local database instance.

### Option 1: Using the init.sql script (Recommended)

1.  **Start the database:**
    ```bash
    docker-compose up -d
    ```

2.  **Run the initialization script:**
    ```bash
    psql -h localhost -U your_username -d soar -f init.sql
    ```
    
    This will:
    - Enable the PostGIS extension
    - Create the `map_layers` table with a spatial index
    - Insert sample data for testing

### Option 2: Manual Setup

1.  **Start the database:**
    ```bash
    docker-compose up -d
    ```

2.  **Connect to the database** using a SQL client (e.g., DBeaver, pgAdmin, or psql). Connection details are in `src/main/resources/application.yml`.

3.  **Enable PostGIS:**
    ```sql
    CREATE EXTENSION IF NOT EXISTS postgis;
    ```
    
4.  **Create the table:**
    ```sql
    CREATE TABLE map_layers (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        geom GEOMETRY(Polygon, 4326) NOT NULL
    );
    
    CREATE INDEX idx_map_layers_geom ON map_layers USING GIST (geom);
    ```

5.  **Insert sample data:**
    ```sql
    INSERT INTO map_layers (name, geom) VALUES
    ('North Africa Region', ST_GeomFromText('POLYGON((20 20, 20 30, 30 30, 30 20, 20 20))', 4326)),
    ('Sahara Desert Section', ST_GeomFromText('POLYGON((22 22, 22 28, 28 28, 28 22, 22 22))', 4326)),
    ('Mediterranean Coast', ST_GeomFromText('POLYGON((15 28, 15 35, 25 35, 25 28, 15 28))', 4326));
    ```

**Note:** The API returns geometry in WKT (Well-Known Text) format for simplicity.

## Running the Application

1.  **Build the application:**
    ```bash
    ./gradlew build
    ```

2.  **Run the application:**
    ```bash
    ./gradlew run
    ```

The application will be running on `http://localhost:8080`.

## API Usage

You can search for map layers by sending a GET request to the `/map-layers/search` endpoint with the latitude and longitude of the point.

**Example:**
```bash
curl "http://localhost:8080/map-layers/search?latitude=25.25306481&longitude=22.27009466"
```

This will return a list of map layers that contain the point with latitude `25.25306481` and longitude `22.27009466`.

**Response Example:**
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

### Testing

A test script is provided for Windows (`test-api.bat`) that runs multiple test queries:

```bash
test-api.bat
```

This will test:
- The provided coordinates (Lat: 25.25306481 / Lng: 22.27009466)
- A point outside all layers
- A point in a different layer

## Additional Documentation

- **PROJECT_SUMMARY.md** - Detailed project architecture and technical information
- **init.sql** - Database initialization script with sample data
- **test-api.bat** - API testing script

