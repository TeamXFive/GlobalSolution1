package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.model.Passagem;
import com.fiap.satellitetracker.service.PassagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Passagens")
@RestController
@RequestMapping("/passagens")
@CrossOrigin(origins = "*")
public class PassagemController {

    private final PassagemService service;

    public PassagemController(PassagemService service) {
        this.service = service;
    }

    @Operation(summary = "Lista as passagens (filtro opcional por localizacaoId)")
    @GetMapping
    public List<Passagem> listar(@RequestParam(required = false) Long localizacaoId) {
        return service.listar(localizacaoId);
    }
}
