package com.example.satellitetracker.data.remote

/**
 * DTOs (Data Transfer Objects) que espelham o JSON devolvido pela API.
 * O Gson preenche os campos pelo nome (mesmo nome do JSON).
 */

// ---- Login ----
data class LoginRequestDto(val email: String, val senha: String)
data class LoginResponseDto(val token: String, val usuario: UsuarioDto)
data class UsuarioDto(val id: Long, val nome: String, val email: String)

// ---- Satelites ----
data class SateliteDto(
    val id: Long,
    val nome: String,
    val tipo: String,
    val regiaoAtual: String?,
    val visivel: Boolean
)

// ---- Localizacao (vem aninhada em passagens/leituras) ----
data class LocalizacaoDto(
    val id: Long,
    val nome: String,
    val cidade: String?,
    val estado: String?
)

// ---- Passagens ----
data class PassagemDto(
    val id: Long,
    val satelite: SateliteDto,
    val horario: String,
    val direcao: String,
    val visivel: Boolean
)

// ---- Leituras de sensor (IoT) ----
data class LeituraDto(
    val id: Long,
    val localizacao: LocalizacaoDto?,
    val temperatura: Int,
    val chovendo: Boolean,
    val nebulosidade: Int,
    val luminosidade: Int,
    val observacaoFavoravel: Boolean
)

// ---- Alertas ----
data class AlertaDto(
    val id: Long,
    val mensagem: String,
    val ativo: Boolean
)
