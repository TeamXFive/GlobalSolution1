package com.fiap.satellitetracker.repository;

import com.fiap.satellitetracker.model.LeituraSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {
    List<LeituraSensor> findByLocalizacaoIdOrderByLidoEmDesc(Long localizacaoId);
}
