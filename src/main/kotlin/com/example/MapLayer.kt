package com.example

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity("map_layers")
data class MapLayer(
    @field:Id
    @field:GeneratedValue
    var id: Long?,
    val name: String,
    val geom: String  // WKT representation for now
)

