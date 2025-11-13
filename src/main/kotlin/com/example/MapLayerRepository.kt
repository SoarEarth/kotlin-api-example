package com.example

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface MapLayerRepository : CrudRepository<MapLayer, Long> {

    @Query("SELECT id, name, ST_AsText(geom) as geom FROM map_layers WHERE ST_Contains(geom, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326))")
    fun findContaining(latitude: Double, longitude: Double): List<MapLayer>
}

