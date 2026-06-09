package com.fiap.satellitetracker.repository;

import com.fiap.satellitetracker.model.Passagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PassagemRepository extends JpaRepository<Passagem, Long> {
    List<Passagem> findByLocalizacaoIdOrderByHorario(Long localizacaoId);
}
