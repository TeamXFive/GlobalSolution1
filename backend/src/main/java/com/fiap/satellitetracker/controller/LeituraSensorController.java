package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.dto.LeituraSensorRequest;
import com.fiap.satellitetracker.model.LeituraSensor;
import com.fiap.satellitetracker.service.LeituraSensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Leituras de Sensor (IoT)")
@RestController
@RequestMapping("/leituras")
@CrossOrigin(origins = "*")
public class LeituraSensorController {

    private final LeituraSensorService service;

    public LeituraSensorController(LeituraSensorService service) {
        this.service = service;
    }

    @Operation(summary = "Lista as leituras de sensores de uma localizacao")
    @GetMapping
    public List<LeituraSensor> listar(@RequestParam Long localizacaoId) {
        return service.listarPorLocalizacao(localizacaoId);
    }

    @Operation(summary = "Registra uma leitura enviada pelo simulador IoT")
    @PostMapping
    public ResponseEntity<LeituraSensor> registrar(@Valid @RequestBody LeituraSensorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(req));
    }
}
