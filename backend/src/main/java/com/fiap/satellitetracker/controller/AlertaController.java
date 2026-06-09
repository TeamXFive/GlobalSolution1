package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.dto.AlertaRequest;
import com.fiap.satellitetracker.model.Alerta;
import com.fiap.satellitetracker.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alertas")
@RestController
@RequestMapping("/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista alertas de um usuario (apenasAtivos opcional)")
    @GetMapping
    public List<Alerta> listar(@RequestParam Long usuarioId,
                               @RequestParam(defaultValue = "false") boolean apenasAtivos) {
        return service.listar(usuarioId, apenasAtivos);
    }

    @Operation(summary = "Cria um alerta")
    @PostMapping
    public ResponseEntity<Alerta> criar(@Valid @RequestBody AlertaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }

    @Operation(summary = "Atualiza um alerta (mensagem / ativo)")
    @PutMapping("/{id}")
    public Alerta atualizar(@PathVariable Long id, @Valid @RequestBody AlertaRequest req) {
        return service.atualizar(id, req);
    }

    @Operation(summary = "Remove um alerta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
