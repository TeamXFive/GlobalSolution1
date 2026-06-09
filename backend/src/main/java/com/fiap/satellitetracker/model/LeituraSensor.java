package com.fiap.satellitetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Entidade "leituras_sensor" - simulacao IoT (Localizacao 1:N LeituraSensor). */
@Entity
@Table(name = "leituras_sensor")
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "localizacao_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    private Localizacao localizacao;

    @Column(nullable = false)
    private Integer temperatura;

    @Column(nullable = false)
    private Boolean chovendo;

    @Column(nullable = false)
    private Integer nebulosidade;

    @Column(nullable = false)
    private Integer luminosidade;

    @Column(name = "observacao_favoravel", nullable = false)
    private Boolean observacaoFavoravel;

    @Column(name = "lido_em", nullable = false)
    private LocalDateTime lidoEm = LocalDateTime.now();

    /**
     * Regra academica de observacao: favoravel quando NAO chove,
     * nebulosidade < 40% e luminosidade < 50%. A mesma regra do app mobile.
     */
    public void calcularObservacao() {
        this.observacaoFavoravel =
                Boolean.FALSE.equals(chovendo) && nebulosidade < 40 && luminosidade < 50;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Localizacao getLocalizacao() { return localizacao; }
    public void setLocalizacao(Localizacao localizacao) { this.localizacao = localizacao; }

    public Integer getTemperatura() { return temperatura; }
    public void setTemperatura(Integer temperatura) { this.temperatura = temperatura; }

    public Boolean getChovendo() { return chovendo; }
    public void setChovendo(Boolean chovendo) { this.chovendo = chovendo; }

    public Integer getNebulosidade() { return nebulosidade; }
    public void setNebulosidade(Integer nebulosidade) { this.nebulosidade = nebulosidade; }

    public Integer getLuminosidade() { return luminosidade; }
    public void setLuminosidade(Integer luminosidade) { this.luminosidade = luminosidade; }

    public Boolean getObservacaoFavoravel() { return observacaoFavoravel; }
    public void setObservacaoFavoravel(Boolean observacaoFavoravel) { this.observacaoFavoravel = observacaoFavoravel; }

    public LocalDateTime getLidoEm() { return lidoEm; }
    public void setLidoEm(LocalDateTime lidoEm) { this.lidoEm = lidoEm; }
}
