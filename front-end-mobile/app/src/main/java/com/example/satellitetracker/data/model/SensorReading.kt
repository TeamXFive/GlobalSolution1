package com.example.satellitetracker.data.model

/** Leitura simulada dos sensores IoT instalados na região do usuário. */
data class SensorReading(
    val location: String,
    val temperature: Int,
    val isRaining: Boolean,
    val cloudiness: Int, // percentual de nuvens no céu (0 a 100)
    val luminosity: Int  // percentual de poluição luminosa (0 a 100)
) {
    /**
     * Lógica acadêmica de observação: o céu é favorável quando
     * não chove, a nebulosidade é menor que 40% e a luminosidade é menor que 50%.
     */
    val isObservationFavorable: Boolean
        get() = !isRaining && cloudiness < 40 && luminosity < 50
}
