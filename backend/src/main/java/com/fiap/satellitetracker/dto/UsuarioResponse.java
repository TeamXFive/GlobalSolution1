package com.fiap.satellitetracker.dto;

import com.fiap.satellitetracker.model.Usuario;

/** Dados de saida de um usuario (NUNCA expoe o hash da senha). */
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;

    public UsuarioResponse(Usuario u) {
        this.id = u.getId();
        this.nome = u.getNome();
        this.email = u.getEmail();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
}
