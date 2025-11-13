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
     * Create a new map layer.
     *
     * @param request The create request containing layer data
     * @return The created map layer
     */
    fun createMapLayer(request: CreateMapLayerRequest): MapLayer {
        logger.info("Creating new map layer: ${request.name}")

        val mapLayer = MapLayer(
            name = request.name,
            geom = request.geom
        )

        val saved = mapLayerRepository.save(mapLayer)
        logger.info("Created map layer with id: ${saved.id}")

        return saved
    }

    /**
     * Find all map layers.
     *
     * @return List of all map layers
     */
    fun findAll(): List<MapLayer> {
        logger.debug("Finding all map layers")
        return mapLayerRepository.findAll().toList()
    }

    /**
     * Find a map layer by ID.
     *
     * @param id The layer ID
     * @return The map layer if found
     */
    fun findById(id: Long): MapLayer? {
        logger.debug("Finding map layer by id: $id")
        return mapLayerRepository.findById(id).orElse(null)
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

