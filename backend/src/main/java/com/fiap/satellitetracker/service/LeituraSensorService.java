package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.dto.LeituraSensorRequest;
import com.fiap.satellitetracker.exception.RecursoNaoEncontradoException;
import com.fiap.satellitetracker.model.LeituraSensor;
import com.fiap.satellitetracker.model.Localizacao;
import com.fiap.satellitetracker.repository.LeituraSensorRepository;
import com.fiap.satellitetracker.repository.LocalizacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeituraSensorService {

    private final LeituraSensorRepository repository;
    private final LocalizacaoRepository localizacaoRepository;

    public LeituraSensorService(LeituraSensorRepository repository,
                                LocalizacaoRepository localizacaoRepository) {
        this.repository = repository;
        this.localizacaoRepository = localizacaoRepository;
    }

    public List<LeituraSensor> listarPorLocalizacao(Long localizacaoId) {
        return repository.findByLocalizacaoIdOrderByLidoEmDesc(localizacaoId);
    }

    /** Recebe uma leitura do simulador IoT e calcula a observacao favoravel. */
    public LeituraSensor registrar(LeituraSensorRequest req) {
        Localizacao loc = localizacaoRepository.findById(req.getLocalizacaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Localizacao nao encontrada: " + req.getLocalizacaoId()));

        LeituraSensor leitura = new LeituraSensor();
        leitura.setLocalizacao(loc);
        leitura.setTemperatura(req.getTemperatura());
        leitura.setChovendo(req.getChovendo());
        leitura.setNebulosidade(req.getNebulosidade());
        leitura.setLuminosidade(req.getLuminosidade());
        leitura.calcularObservacao(); // aplica a regra academica
        return repository.save(leitura);
    }
}
