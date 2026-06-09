package com.fiap.satellitetracker.controller;

import com.fiap.satellitetracker.dto.LoginRequest;
import com.fiap.satellitetracker.dto.LoginResponse;
import com.fiap.satellitetracker.dto.UsuarioRequest;
import com.fiap.satellitetracker.dto.UsuarioResponse;
import com.fiap.satellitetracker.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuarios e Login")
@RestController
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo usuario (senha gravada com hash BCrypt)")
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }

    @Operation(summary = "Autentica o usuario e devolve um token JWT")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }
}
