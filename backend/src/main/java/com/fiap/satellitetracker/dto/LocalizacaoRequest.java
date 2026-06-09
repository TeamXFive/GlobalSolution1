package com.fiap.satellitetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Dados de entrada para criar uma localizacao. */
public class LocalizacaoRequest {

    @NotNull(message = "O usuarioId e obrigatorio")
    private Long usuarioId;

    @NotBlank(message = "O nome da localizacao e obrigatorio")
    private String nome;

    private String cidade;
    private String estado;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
}
