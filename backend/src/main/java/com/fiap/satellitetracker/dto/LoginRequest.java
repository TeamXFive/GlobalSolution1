package com.fiap.satellitetracker.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciais de login. */
public class LoginRequest {

    @NotBlank(message = "O email e obrigatorio")
    private String email;

    @NotBlank(message = "A senha e obrigatoria")
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
