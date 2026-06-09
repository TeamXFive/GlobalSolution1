package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.exception.RecursoNaoEncontradoException;
import com.fiap.satellitetracker.model.Satelite;
import com.fiap.satellitetracker.repository.SateliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SateliteService {

    private final SateliteRepository repository;

    public SateliteService(SateliteRepository repository) {
        this.repository = repository;
    }

    /** Lista todos os satelites; se informado, filtra por nome. */
    public List<Satelite> listar(String nome) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome);
        }
        return repository.findAll();
    }

    public Satelite buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Satelite nao encontrado: " + id));
    }
}
