package com.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.util.*

/**
 * Unit tests for MapLayerService.
 * Tests business logic in isolation using mocks.
 */
class MapLayerServiceTest {

    private val mockRepository = mock(MapLayerRepository::class.java)
    private val service = MapLayerService(mockRepository)

    @Test
    fun `findLayersContainingPoint should validate coordinates`() {
        assertThrows<IllegalArgumentException> {
            service.findLayersContainingPoint(91.0, 0.0)
        }

        assertThrows<IllegalArgumentException> {
            service.findLayersContainingPoint(-91.0, 0.0)
        }

        assertThrows<IllegalArgumentException> {
            service.findLayersContainingPoint(0.0, 181.0)
        }

        assertThrows<IllegalArgumentException> {
            service.findLayersContainingPoint(0.0, -181.0)
        }
    }

    @Test
    fun `findLayersContainingPoint should return layers from repository`() {
        val expectedLayers = listOf(
            MapLayer(1L, "Layer 1", "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))"),
            MapLayer(2L, "Layer 2", "POLYGON((5 5, 5 15, 15 15, 15 5, 5 5))")
        )

        whenever(mockRepository.findContaining(25.0, 22.0)).thenReturn(expectedLayers)

        val result = service.findLayersContainingPoint(25.0, 22.0)

        assertEquals(2, result.size)
        assertEquals(expectedLayers, result)
        verify(mockRepository).findContaining(25.0, 22.0)
    }
}

