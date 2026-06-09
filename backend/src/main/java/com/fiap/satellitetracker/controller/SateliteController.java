package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.model.Satelite;
import com.fiap.satellitetracker.service.SateliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Satelites")
@RestController
@RequestMapping("/satelites")
@CrossOrigin(origins = "*")
public class SateliteController {

    private final SateliteService service;

    public SateliteController(SateliteService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os satelites (filtro opcional por nome)")
    @GetMapping
    public List<Satelite> listar(@RequestParam(required = false) String nome) {
        return service.listar(nome);
    }

    @Operation(summary = "Busca um satelite por id")
    @GetMapping("/{id}")
    public Satelite buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
