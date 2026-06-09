package com.example.satellitetracker.data

import com.example.satellitetracker.data.model.Satellite
import com.example.satellitetracker.data.model.SatellitePass
import com.example.satellitetracker.data.model.SensorReading

object MockData {

    const val VALID_EMAIL = "teste@email.com"
    const val VALID_PASSWORD = "123456"

    const val DEFAULT_LOCATION = "Niterói - RJ"

    val SUGGESTED_LOCATIONS = listOf(
        "Niterói - RJ",
        "Rio de Janeiro - RJ",
        "São Paulo - SP",
        "Campinas - SP",
        "Belo Horizonte - MG",
        "Curitiba - PR",
        "Porto Alegre - RS",
        "Florianópolis - SC",
        "Salvador - BA",
        "Recife - PE",
        "Fortaleza - CE",
        "Brasília - DF",
        "Manaus - AM",
        "Belém - PA",
        "Goiânia - GO"
    )

    val nextPass = SatellitePass(
        satelliteName = "ISS",
        time = "20:42",
        direction = "Noroeste → Sudeste",
        isVisible = true
    )

    val sensorReading = SensorReading(
        location = DEFAULT_LOCATION,
        temperature = 24,
        isRaining = false,
        cloudiness = 20,
        luminosity = 18
    )

    val alertMessage =
        "A ISS passará sobre sua região às ${nextPass.time}. Condições favoráveis para observação."

    val satellites = listOf(
        Satellite(1, "ISS", "Estação Espacial", "20:42", "Noroeste → Sudeste", true, "Sobre o Oceano Atlântico Sul"),
        Satellite(2, "Sentinel-2", "Observação da Terra", "21:15", "Norte → Sul", false, "Sobre a Europa Central"),
        Satellite(3, "Landsat 8", "Monitoramento Ambiental", "22:05", "Oeste → Leste", false, "Sobre a América do Norte"),
        Satellite(4, "Amazonia-1", "Satélite Brasileiro de Observação", "23:10", "Sul → Norte", false, "Sobre a Floresta Amazônica"),
        Satellite(5, "Hubble", "Telescópio Espacial", "23:48", "Oeste → Leste", false, "Sobre o Oceano Pacífico"),
        Satellite(6, "Starlink-1130", "Internet por Satélite", "00:20", "Norte → Sul", true, "Sobre a África Ocidental"),
        Satellite(7, "NOAA-19", "Satélite Meteorológico", "01:05", "Sul → Norte", false, "Sobre a Ásia Oriental"),
        Satellite(8, "CBERS-04A", "Observação da Terra (Brasil-China)", "02:30", "Norte → Sul", false, "Sobre a Oceania")
    )
}
