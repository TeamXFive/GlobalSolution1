package com.example.satellitetracker.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/** Endpoints da API consumidos pelo app (Retrofit gera a implementacao). */
interface ApiService {

    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @GET("satelites")
    suspend fun getSatelites(@Query("nome") nome: String?): List<SateliteDto>

    @GET("passagens")
    suspend fun getPassagens(@Query("localizacaoId") localizacaoId: Long): List<PassagemDto>

    @GET("leituras")
    suspend fun getLeituras(@Query("localizacaoId") localizacaoId: Long): List<LeituraDto>

    @GET("alertas")
    suspend fun getAlertas(
        @Query("usuarioId") usuarioId: Long,
        @Query("apenasAtivos") apenasAtivos: Boolean
    ): List<AlertaDto>
}
