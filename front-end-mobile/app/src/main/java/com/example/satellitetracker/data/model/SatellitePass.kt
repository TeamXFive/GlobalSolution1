package com.example.satellitetracker.data.model

/** Próxima passagem de satélite sobre a região pesquisada pelo usuário. */
data class SatellitePass(
    val satelliteName: String,
    val time: String,
    val direction: String,
    val isVisible: Boolean
)
