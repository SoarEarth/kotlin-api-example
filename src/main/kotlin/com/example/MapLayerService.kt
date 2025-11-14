package com.example

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

/**
 * Service layer for map layer business logic.
 * Separates business logic from controllers and provides transaction management.
 */
@Singleton
class MapLayerService(private val mapLayerRepository: MapLayerRepository) {

    private val logger = LoggerFactory.getLogger(MapLayerService::class.java)

    /**
     * Find all map layers that contain the given point.
     *
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @return List of map layers containing the point
     * @throws IllegalArgumentException if coordinates are invalid
     */
    fun findLayersContainingPoint(latitude: Double, longitude: Double): List<MapLayer> {
        validateCoordinates(latitude, longitude)

        logger.info("Finding layers containing point: lat=$latitude, lon=$longitude")

        val layers = mapLayerRepository.findContaining(latitude, longitude)

        logger.info("Found ${layers.size} layers containing the point")

        return layers
    }

    /**
     * Validate that coordinates are within valid ranges.
     */
    private fun validateCoordinates(latitude: Double, longitude: Double) {
        if (latitude < -90 || latitude > 90) {
            throw IllegalArgumentException("Latitude must be between -90 and 90, got: $latitude")
        }
        if (longitude < -180 || longitude > 180) {
            throw IllegalArgumentException("Longitude must be between -180 and 180, got: $longitude")
        }
    }
}

