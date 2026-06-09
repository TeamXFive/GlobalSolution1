package com.fiap.satellitetracker.model;

import jakarta.persistence.*;

/** Entidade que representa a tabela "satelites". */
@Entity
@Table(name = "satelites")
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 120)
    private String tipo;

    @Column(name = "regiao_atual", length = 160)
    private String regiaoAtual;

    @Column(nullable = false)
    private Boolean visivel = false;

    public Satelite() { }

    public Satelite(String nome, String tipo, String regiaoAtual, Boolean visivel) {
        this.nome = nome;
        this.tipo = tipo;
        this.regiaoAtual = regiaoAtual;
        this.visivel = visivel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRegiaoAtual() { return regiaoAtual; }
    public void setRegiaoAtual(String regiaoAtual) { this.regiaoAtual = regiaoAtual; }

    public Boolean getVisivel() { return visivel; }
    public void setVisivel(Boolean visivel) { this.visivel = visivel; }
}
