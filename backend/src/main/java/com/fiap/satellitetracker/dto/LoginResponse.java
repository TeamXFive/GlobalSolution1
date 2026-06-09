package com.fiap.satellitetracker.dto;

/** Resposta do login: token JWT (opcional) + dados basicos do usuario. */
public class LoginResponse {

    private final String token;
    private final UsuarioResponse usuario;

    public LoginResponse(String token, UsuarioResponse usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public UsuarioResponse getUsuario() { return usuario; }
}
