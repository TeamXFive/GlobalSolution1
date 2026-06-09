package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.dto.LoginRequest;
import com.fiap.satellitetracker.dto.LoginResponse;
import com.fiap.satellitetracker.dto.UsuarioRequest;
import com.fiap.satellitetracker.dto.UsuarioResponse;
import com.fiap.satellitetracker.exception.NegocioException;
import com.fiap.satellitetracker.model.Usuario;
import com.fiap.satellitetracker.repository.UsuarioRepository;
import com.fiap.satellitetracker.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /** Cria um usuario, gravando a senha com hash BCrypt. */
    public UsuarioResponse criar(UsuarioRequest req) {
        if (repository.existsByEmail(req.getEmail())) {
            throw new NegocioException("Ja existe um usuario com este email");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(req.getNome());
        usuario.setEmail(req.getEmail());
        // BCrypt: a senha NUNCA e gravada em texto puro.
        usuario.setSenhaHash(passwordEncoder.encode(req.getSenha()));
        return new UsuarioResponse(repository.save(usuario));
    }

    /** Valida credenciais e devolve um token JWT em caso de sucesso. */
    public LoginResponse login(LoginRequest req) {
        Usuario usuario = repository.findByEmail(req.getEmail())
                .orElseThrow(() -> new NegocioException("Email ou senha invalidos"));

        // Compara a senha enviada com o hash armazenado.
        if (!passwordEncoder.matches(req.getSenha(), usuario.getSenhaHash())) {
            throw new NegocioException("Email ou senha invalidos");
        }
        String token = jwtService.gerarToken(usuario.getEmail());
        return new LoginResponse(token, new UsuarioResponse(usuario));
    }
}
