package com.fiap.satellitetracker.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Geracao de token JWT (opcional). No login, devolvemos um token assinado
 * contendo o email do usuario. Demonstra o conceito de autenticacao por token.
 */
@Service
public class JwtService {

    // Chave de exemplo (em producao viria de variavel de ambiente / secret).
    private static final String SECRET =
            "chave-secreta-academica-fiap-global-solution-satelite-1234567890";
    private static final long EXPIRACAO_MS = 1000L * 60 * 60; // 1 hora

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String gerarToken(String email) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + EXPIRACAO_MS))
                .signWith(key)
                .compact();
    }
}
