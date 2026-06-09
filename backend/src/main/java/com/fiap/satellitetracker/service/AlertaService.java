package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.dto.AlertaRequest;
import com.fiap.satellitetracker.exception.RecursoNaoEncontradoException;
import com.fiap.satellitetracker.model.Alerta;
import com.fiap.satellitetracker.model.Usuario;
import com.fiap.satellitetracker.repository.AlertaRepository;
import com.fiap.satellitetracker.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public AlertaService(AlertaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    /** Lista alertas de um usuario; se apenasAtivos=true, filtra os ativos. */
    public List<Alerta> listar(Long usuarioId, boolean apenasAtivos) {
        if (apenasAtivos) {
            return repository.findByUsuarioIdAndAtivoTrue(usuarioId);
        }
        return repository.findByUsuarioId(usuarioId);
    }

    public Alerta criar(AlertaRequest req) {
        Usuario usuario = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuario nao encontrado: " + req.getUsuarioId()));
        Alerta alerta = new Alerta();
        alerta.setUsuario(usuario);
        alerta.setMensagem(req.getMensagem());
        alerta.setAtivo(req.getAtivo() == null ? Boolean.TRUE : req.getAtivo());
        return repository.save(alerta);
    }

    /** PUT /alertas/{id} - atualiza mensagem e/ou status do alerta. */
    public Alerta atualizar(Long id, AlertaRequest req) {
        Alerta alerta = buscar(id);
        alerta.setMensagem(req.getMensagem());
        if (req.getAtivo() != null) {
            alerta.setAtivo(req.getAtivo());
        }
        return repository.save(alerta);
    }

    /** DELETE /alertas/{id} */
    public void deletar(Long id) {
        Alerta alerta = buscar(id);
        repository.delete(alerta);
    }

    private Alerta buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta nao encontrado: " + id));
    }
}
