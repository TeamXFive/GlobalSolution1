package com.fiap.satellitetracker.repository;

import com.fiap.satellitetracker.model.Satelite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SateliteRepository extends JpaRepository<Satelite, Long> {
    // Busca por nome (parametrizada -> protegida contra SQL Injection).
    List<Satelite> findByNomeContainingIgnoreCase(String nome);
}
