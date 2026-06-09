package com.fiap.satellitetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/** Entidade "passagens" (Satelite 1:N Passagem, Localizacao 1:N Passagem). */
@Entity
@Table(name = "passagens")
public class Passagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "satelite_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Satelite satelite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "localizacao_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    private Localizacao localizacao;

    @Column(nullable = false, length = 5)
    private String horario;

    @Column(nullable = false, length = 60)
    private String direcao;

    @Column(nullable = false)
    private Boolean visivel = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Satelite getSatelite() { return satelite; }
    public void setSatelite(Satelite satelite) { this.satelite = satelite; }

    public Localizacao getLocalizacao() { return localizacao; }
    public void setLocalizacao(Localizacao localizacao) { this.localizacao = localizacao; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }

    public Boolean getVisivel() { return visivel; }
    public void setVisivel(Boolean visivel) { this.visivel = visivel; }
}
