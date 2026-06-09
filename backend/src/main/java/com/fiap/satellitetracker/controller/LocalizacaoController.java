package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.dto.LocalizacaoRequest;
import com.fiap.satellitetracker.model.Localizacao;
import com.fiap.satellitetracker.service.LocalizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Localizacoes")
@RestController
@RequestMapping("/localizacoes")
@CrossOrigin(origins = "*")
public class LocalizacaoController {

    private final LocalizacaoService service;

    public LocalizacaoController(LocalizacaoService service) {
        this.service = service;
    }

    @Operation(summary = "Lista as localizacoes de um usuario")
    @GetMapping
    public List<Localizacao> listar(@RequestParam Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @Operation(summary = "Cria uma localizacao para um usuario")
    @PostMapping
    public ResponseEntity<Localizacao> criar(@Valid @RequestBody LocalizacaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }
}
