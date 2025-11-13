package com.example

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

/**
 * Repository interface for MapLayer entity operations.
 * Provides database access with PostGIS spatial query support.
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
interface MapLayerRepository : CrudRepository<MapLayer, Long> {

    /**
     * Find all map layers that contain a specific geographic point.
     * Uses PostGIS ST_Contains function to perform spatial query.
     *
     * @param latitude The latitude of the point (Y coordinate)
     * @param longitude The longitude of the point (X coordinate)
     * @return List of map layers containing the point
     */
    @Query("SELECT id, name, ST_AsText(geom) as geom FROM map_layers WHERE ST_Contains(geom, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326))")
    fun findContaining(latitude: Double, longitude: Double): List<MapLayer>
}



