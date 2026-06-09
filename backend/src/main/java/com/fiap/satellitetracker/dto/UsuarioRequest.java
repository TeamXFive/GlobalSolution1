package com.fiap.satellitetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Dados de entrada para criar um usuario (com validacao). */
public class UsuarioRequest {

    @NotBlank(message = "O nome e obrigatorio")
    private String nome;

    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "A senha e obrigatoria")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
