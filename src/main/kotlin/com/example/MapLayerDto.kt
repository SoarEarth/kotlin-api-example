package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

/**
 * Data Transfer Object for MapLayer responses.
 * Separates the API contract from the database entity.
 */
data class MapLayerDto(
    val id: Long,
    val name: String,
    val geom: String
) {
    companion object {
        fun from(mapLayer: MapLayer): MapLayerDto {
            return MapLayerDto(
                id = mapLayer.id ?: throw IllegalStateException("MapLayer id cannot be null"),
                name = mapLayer.name,
                geom = mapLayer.geom
            )
        }
    }
}

/**
 * Request DTO for creating a new map layer.
 */
data class CreateMapLayerRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Geometry is required")
    val geom: String
)

/**
 * Response DTO for search results with additional context.
 */
data class SearchResultDto(
    @JsonProperty("query")
    val query: LocationQuery,

    @JsonProperty("results")
    val results: List<MapLayerDto>,

    @JsonProperty("count")
    val count: Int
)

/**
 * DTO representing the location query parameters.
 */
data class LocationQuery(
    val latitude: Double,
    val longitude: Double
)

