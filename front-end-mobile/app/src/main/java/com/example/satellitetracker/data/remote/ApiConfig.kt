package com.example.satellitetracker.data.remote

/**
 * Configuracao do acesso a API do backend.
 *
 * IMPORTANTE - escolha da BASE_URL conforme onde o app roda:
 *  - EMULADOR Android  -> "http://10.0.2.2:8080/"  (10.0.2.2 = "localhost" do PC)
 *  - CELULAR FISICO    -> "http://SEU_IP_LOCAL:8080/" (ex.: http://192.168.0.10:8080/)
 *                         (descubra o IP do PC com "ipconfig" / "ifconfig")
 *
 * O backend precisa estar rodando no PC (ver GS/backend/README.md).
 */
object ApiConfig {

    const val BASE_URL = "http://10.0.2.2:8080/"

    // Ids do usuario/localizacao usados na demonstracao (mesmos do seed da API).
    const val DEFAULT_USER_ID = 1L
    const val DEFAULT_LOCATION_ID = 1L
}
