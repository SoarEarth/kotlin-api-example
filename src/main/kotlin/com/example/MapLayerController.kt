package com.example

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory

/**
 * REST API controller for map layer operations.
 * Provides endpoints for searching and managing map layers with spatial data.
 */
@Controller("/api/v1/map-layers")
@Validated
class MapLayerController(private val mapLayerService: MapLayerService) {

    private val logger = LoggerFactory.getLogger(MapLayerController::class.java)

    /**
     * Search for map layers containing a specific geographic point.
     *
     * @param latitude The latitude coordinate (-90 to 90)
     * @param longitude The longitude coordinate (-180 to 180)
     * @return Search results with matching layers
     */
    @Get("/search")
    fun findLayersContainingPoint(
        @QueryValue
        @Min(-90, message = "Latitude must be >= -90")
        @Max(90, message = "Latitude must be <= 90")
        latitude: Double,

        @QueryValue
        @Min(-180, message = "Longitude must be >= -180")
        @Max(180, message = "Longitude must be <= 180")
        longitude: Double
    ): HttpResponse<SearchResultDto> {
        return try {
            logger.info("Received search request for point: lat=$latitude, lon=$longitude")

            val layers = mapLayerService.findLayersContainingPoint(latitude, longitude)
            val dtos = layers.map { MapLayerDto.from(it) }

            val response = SearchResultDto(
                query = LocationQuery(latitude, longitude),
                results = dtos,
                count = dtos.size
            )

            HttpResponse.ok(response)
        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid coordinates: ${e.message}")
            HttpResponse.badRequest()
        } catch (e: Exception) {
            logger.error("Error searching for layers", e)
            HttpResponse.serverError()
        }
    }

}


