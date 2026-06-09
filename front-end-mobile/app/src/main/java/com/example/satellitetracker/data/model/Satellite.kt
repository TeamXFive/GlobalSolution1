package com.example.satellitetracker.data.model

/**
 * Representa um satélite público monitorado pelo app.
 * Os dados são mockados para o protótipo acadêmico.
 */
data class Satellite(
    val id: Int,
    val name: String,
    val type: String,
    val nextPassTime: String,
    val direction: String,
    val isVisible: Boolean,
    val currentRegion: String
)
