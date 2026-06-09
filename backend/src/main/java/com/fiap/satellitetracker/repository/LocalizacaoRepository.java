package com.fiap.satellitetracker.repository;

import com.fiap.satellitetracker.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    List<Localizacao> findByUsuarioId(Long usuarioId);
}
