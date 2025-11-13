package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

/**
 * Integration tests for MapLayerController.
 * Tests the REST API endpoints with actual HTTP requests.
 */
@MicronautTest
class MapLayerControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `test search endpoint with valid coordinates`() {
        val request = HttpRequest.GET<SearchResultDto>("/api/v1/map-layers/search?latitude=25.25&longitude=22.27")
        val response = client.toBlocking().exchange(request, SearchResultDto::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

        val result = response.body()!!
        assertEquals(25.25, result.query.latitude)
        assertEquals(22.27, result.query.longitude)
        assertTrue(result.count >= 0)
    }

    @Test
    fun `test search endpoint with invalid latitude`() {
        val request = HttpRequest.GET<ErrorResponse>("/api/v1/map-layers/search?latitude=100&longitude=22.27")

        try {
            client.toBlocking().exchange(request, ErrorResponse::class.java)
            fail("Expected exception for invalid latitude")
        } catch (e: Exception) {
            // Expected - validation should reject this
        }
    }

    @Test
    fun `test search endpoint with invalid longitude`() {
        val request = HttpRequest.GET<ErrorResponse>("/api/v1/map-layers/search?latitude=25&longitude=200")

        try {
            client.toBlocking().exchange(request, ErrorResponse::class.java)
            fail("Expected exception for invalid longitude")
        } catch (e: Exception) {
            // Expected - validation should reject this
        }
    }

    @Test
    fun `test get all layers endpoint`() {
        val request = HttpRequest.GET<List<MapLayerDto>>("/api/v1/map-layers")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Test
    fun `test create layer endpoint`() {
        val createRequest = CreateMapLayerRequest(
            name = "Test Layer",
            geom = "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))"
        )

        val request = HttpRequest.POST("/api/v1/map-layers", createRequest)
        val response = client.toBlocking().exchange(request, MapLayerDto::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertNotNull(response.body())

        val created = response.body()!!
        assertEquals("Test Layer", created.name)
        assertNotNull(created.id)
    }
}

