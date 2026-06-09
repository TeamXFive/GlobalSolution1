package com.example.satellitetracker.data

import com.example.satellitetracker.data.model.Satellite
import com.example.satellitetracker.data.model.SatellitePass
import com.example.satellitetracker.data.model.SensorReading
import com.example.satellitetracker.data.remote.ApiConfig
import com.example.satellitetracker.data.remote.LeituraDto
import com.example.satellitetracker.data.remote.LoginRequestDto
import com.example.satellitetracker.data.remote.LoginResponseDto
import com.example.satellitetracker.data.remote.RetrofitClient
import com.example.satellitetracker.data.remote.SateliteDto
import retrofit2.HttpException
import java.io.IOException

/** Dados combinados da tela Home. */
data class HomeData(val nextPass: SatellitePass?, val reading: SensorReading?)

/** Dados combinados da tela de Alertas. */
data class AlertsData(val reading: SensorReading?, val alertMessage: String?)

/**
 * Ponte entre o app e a API do backend.
 * Faz as chamadas (Retrofit) e converte os DTOs nos modelos usados pela UI.
 */
object Repository {

    private val api = RetrofitClient.api

    // ---- Login ----
    suspend fun login(email: String, senha: String): LoginResponseDto =
        api.login(LoginRequestDto(email, senha))

    // ---- Lista de satelites (enriquecida com a passagem, quando existir) ----
    suspend fun getSatellites(): List<Satellite> {
        val satelites = api.getSatelites(nome = null)
        // A passagem (horario/direcao) vem da localizacao padrao; se falhar, segue sem ela.
        val passByName = runCatching {
            api.getPassagens(ApiConfig.DEFAULT_LOCATION_ID).associateBy { it.satelite.nome }
        }.getOrDefault(emptyMap())

        return satelites.map { dto ->
            val pass = passByName[dto.nome]
            dto.toSatellite(
                nextPassTime = pass?.horario ?: "—",
                direction = pass?.direcao ?: "—"
            )
        }
    }

    // ---- Dados da Home (proxima passagem + condicao do ceu) ----
    suspend fun getHomeData(): HomeData {
        val nextPass = api.getPassagens(ApiConfig.DEFAULT_LOCATION_ID).firstOrNull()?.let {
            SatellitePass(
                satelliteName = it.satelite.nome,
                time = it.horario,
                direction = it.direcao,
                isVisible = it.visivel
            )
        }
        val reading = api.getLeituras(ApiConfig.DEFAULT_LOCATION_ID).firstOrNull()?.toReading()
        return HomeData(nextPass, reading)
    }

    // ---- Dados da tela de Alertas (sensores + mensagem de alerta) ----
    suspend fun getAlertsData(): AlertsData {
        val reading = api.getLeituras(ApiConfig.DEFAULT_LOCATION_ID).firstOrNull()?.toReading()
        val alertMessage = api.getAlertas(ApiConfig.DEFAULT_USER_ID, apenasAtivos = true)
            .firstOrNull()?.mensagem
        return AlertsData(reading, alertMessage)
    }

    // -------- Mapeadores DTO -> modelo da UI --------
    private fun SateliteDto.toSatellite(nextPassTime: String, direction: String) = Satellite(
        id = id.toInt(),
        name = nome,
        type = tipo,
        nextPassTime = nextPassTime,
        direction = direction,
        isVisible = visivel,
        currentRegion = regiaoAtual ?: "—"
    )

    private fun LeituraDto.toReading() = SensorReading(
        location = localizacao?.nome ?: MockData.DEFAULT_LOCATION,
        temperature = temperatura,
        isRaining = chovendo,
        cloudiness = nebulosidade,
        luminosity = luminosidade
    )
}

/** Converte um erro de rede em uma mensagem amigavel para o usuario. */
fun apiErrorMessage(e: Throwable): String = when (e) {
    is HttpException -> when (e.code()) {
        400, 401 -> "Email ou senha incorretos."
        404 -> "Recurso nao encontrado no servidor."
        else -> "Erro do servidor (codigo ${e.code()})."
    }
    is IOException ->
        "Nao foi possivel conectar ao servidor. Confira se o backend esta rodando no PC."
    else -> e.message ?: "Ocorreu um erro inesperado."
}
