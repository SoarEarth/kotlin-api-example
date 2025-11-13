package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller("/map-layers")
class MapLayerController(private val mapLayerRepository: MapLayerRepository) {

    @Get("/search")
    fun findLayersContainingPoint(
        @QueryValue latitude: Double,
        @QueryValue longitude: Double
    ): List<MapLayer> {
        return mapLayerRepository.findContaining(latitude, longitude)
    }
}

