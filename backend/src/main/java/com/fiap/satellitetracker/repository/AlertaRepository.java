package com.fiap.satellitetracker.repository;

import com.fiap.satellitetracker.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByUsuarioId(Long usuarioId);
    List<Alerta> findByUsuarioIdAndAtivoTrue(Long usuarioId);
}
