package com.fiap.satellitetracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Dados enviados pelo simulador IoT (Python) ao registrar uma leitura. */
public class LeituraSensorRequest {

    @NotNull(message = "O localizacaoId e obrigatorio")
    private Long localizacaoId;

    @NotNull(message = "A temperatura e obrigatoria")
    private Integer temperatura;

    @NotNull(message = "O campo chovendo e obrigatorio")
    private Boolean chovendo;

    @NotNull(message = "A nebulosidade e obrigatoria")
    @Min(value = 0, message = "A nebulosidade deve ser >= 0")
    @Max(value = 100, message = "A nebulosidade deve ser <= 100")
    private Integer nebulosidade;

    @NotNull(message = "A luminosidade e obrigatoria")
    @Min(value = 0, message = "A luminosidade deve ser >= 0")
    @Max(value = 100, message = "A luminosidade deve ser <= 100")
    private Integer luminosidade;

    public Long getLocalizacaoId() { return localizacaoId; }
    public void setLocalizacaoId(Long localizacaoId) { this.localizacaoId = localizacaoId; }

    public Integer getTemperatura() { return temperatura; }
    public void setTemperatura(Integer temperatura) { this.temperatura = temperatura; }

    public Boolean getChovendo() { return chovendo; }
    public void setChovendo(Boolean chovendo) { this.chovendo = chovendo; }

    public Integer getNebulosidade() { return nebulosidade; }
    public void setNebulosidade(Integer nebulosidade) { this.nebulosidade = nebulosidade; }

    public Integer getLuminosidade() { return luminosidade; }
    public void setLuminosidade(Integer luminosidade) { this.luminosidade = luminosidade; }
}
