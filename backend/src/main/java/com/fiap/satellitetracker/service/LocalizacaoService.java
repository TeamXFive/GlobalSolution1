package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.dto.LocalizacaoRequest;
import com.fiap.satellitetracker.exception.RecursoNaoEncontradoException;
import com.fiap.satellitetracker.model.Localizacao;
import com.fiap.satellitetracker.model.Usuario;
import com.fiap.satellitetracker.repository.LocalizacaoRepository;
import com.fiap.satellitetracker.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizacaoService {

    private final LocalizacaoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public LocalizacaoService(LocalizacaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Localizacao> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public Localizacao criar(LocalizacaoRequest req) {
        Usuario usuario = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuario nao encontrado: " + req.getUsuarioId()));

        Localizacao loc = new Localizacao();
        loc.setUsuario(usuario);
        loc.setNome(req.getNome());
        loc.setCidade(req.getCidade());
        loc.setEstado(req.getEstado());
        loc.setLatitude(req.getLatitude());
        loc.setLongitude(req.getLongitude());
        return repository.save(loc);
    }
}
