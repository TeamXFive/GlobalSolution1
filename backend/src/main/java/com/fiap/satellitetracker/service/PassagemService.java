package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.model.Passagem;
import com.fiap.satellitetracker.repository.PassagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassagemService {

    private final PassagemRepository repository;

    public PassagemService(PassagemRepository repository) {
        this.repository = repository;
    }

    /** Lista passagens; se informado, filtra pela localizacao. */
    public List<Passagem> listar(Long localizacaoId) {
        if (localizacaoId != null) {
            return repository.findByLocalizacaoIdOrderByHorario(localizacaoId);
        }
        return repository.findAll();
    }
}
