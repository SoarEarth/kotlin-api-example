-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create the map_layers table
CREATE TABLE IF NOT EXISTS map_layers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL
);

-- Create a spatial index for better performance
CREATE INDEX IF NOT EXISTS idx_map_layers_geom ON map_layers USING GIST (geom);

-- Insert sample data
-- Layer 1: Covers coordinates around (25, 25)
INSERT INTO map_layers (name, geom) VALUES
('North Africa Region', ST_GeomFromText('POLYGON((20 20, 20 30, 30 30, 30 20, 20 20))', 4326)),
('Sahara Desert Section', ST_GeomFromText('POLYGON((22 22, 22 28, 28 28, 28 22, 22 22))', 4326)),
('Mediterranean Coast', ST_GeomFromText('POLYGON((15 28, 15 35, 25 35, 25 28, 15 28))', 4326));

-- The test point (Lat: 25.25306481 / Lng: 22.27009466) should be contained in the first two layers

