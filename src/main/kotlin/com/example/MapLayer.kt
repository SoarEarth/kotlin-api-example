package com.example

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import jakarta.validation.constraints.NotBlank

/**
 * Entity representing a map layer with spatial geometry.
 *
 * @property id The unique identifier for the map layer
 * @property name The name of the map layer
 * @property geom The geometry in WKT (Well-Known Text) format
 */
@MappedEntity("map_layers")
data class MapLayer(
    @field:Id
    @field:GeneratedValue
    var id: Long? = null,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val geom: String  // WKT representation for now
)

