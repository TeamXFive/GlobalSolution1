package com.fiap.satellitetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Dados para criar ou atualizar um alerta. */
public class AlertaRequest {

    @NotNull(message = "O usuarioId e obrigatorio")
    private Long usuarioId;

    @NotBlank(message = "A mensagem e obrigatoria")
    private String mensagem;

    private Boolean ativo;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
