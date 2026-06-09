package com.fiap.satellitetracker.config;

import com.fiap.satellitetracker.model.*;
import com.fiap.satellitetracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Popula o banco com os MESMOS dados mockados do app mobile na primeira
 * execucao (se ainda nao houver usuarios). Assim a API ja funciona para a
 * demonstracao, tanto em H2 quanto em PostgreSQL vazio.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(UsuarioRepository usuarioRepo,
                           SateliteRepository sateliteRepo,
                           LocalizacaoRepository localizacaoRepo,
                           PassagemRepository passagemRepo,
                           AlertaRepository alertaRepo,
                           LeituraSensorRepository leituraRepo,
                           PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepo.count() > 0) {
                return; // ja foi populado
            }

            // ---- Usuario de teste (senha "123456" com hash BCrypt) ----
            Usuario teste = new Usuario();
            teste.setNome("Usuario Teste");
            teste.setEmail("teste@email.com");
            teste.setSenhaHash(encoder.encode("123456"));
            usuarioRepo.save(teste);

            // ---- Satelites (mesmos do MockData.kt) ----
            List<Satelite> satelites = sateliteRepo.saveAll(List.of(
                new Satelite("ISS", "Estacao Espacial", "Sobre o Oceano Atlantico Sul", true),
                new Satelite("Sentinel-2", "Observacao da Terra", "Sobre a Europa Central", false),
                new Satelite("Landsat 8", "Monitoramento Ambiental", "Sobre a America do Norte", false),
                new Satelite("Amazonia-1", "Satelite Brasileiro de Observacao", "Sobre a Floresta Amazonica", false),
                new Satelite("Hubble", "Telescopio Espacial", "Sobre o Oceano Pacifico", false),
                new Satelite("Starlink-1130", "Internet por Satelite", "Sobre a Africa Ocidental", true),
                new Satelite("NOAA-19", "Satelite Meteorologico", "Sobre a Asia Oriental", false),
                new Satelite("CBERS-04A", "Observacao da Terra (Brasil-China)", "Sobre a Oceania", false)
            ));

            // ---- Localizacao Niteroi - RJ ----
            Localizacao niteroi = new Localizacao();
            niteroi.setUsuario(teste);
            niteroi.setNome("Niteroi - RJ");
            niteroi.setCidade("Niteroi");
            niteroi.setEstado("RJ");
            niteroi.setLatitude(new BigDecimal("-22.883300"));
            niteroi.setLongitude(new BigDecimal("-43.103600"));
            localizacaoRepo.save(niteroi);

            // ---- Passagens sobre Niteroi ----
            passagemRepo.saveAll(List.of(
                novaPassagem(satelites.get(0), niteroi, "20:42", "Noroeste -> Sudeste", true),
                novaPassagem(satelites.get(1), niteroi, "21:15", "Norte -> Sul", false),
                novaPassagem(satelites.get(2), niteroi, "22:05", "Oeste -> Leste", false),
                novaPassagem(satelites.get(3), niteroi, "23:10", "Sul -> Norte", false)
            ));

            // ---- Alerta ----
            Alerta alerta = new Alerta();
            alerta.setUsuario(teste);
            alerta.setMensagem("A ISS passara sobre sua regiao as 20:42. Condicoes favoraveis para observacao.");
            alerta.setAtivo(true);
            alertaRepo.save(alerta);

            // ---- Leitura de sensor IoT (favoravel) ----
            LeituraSensor leitura = new LeituraSensor();
            leitura.setLocalizacao(niteroi);
            leitura.setTemperatura(24);
            leitura.setChovendo(false);
            leitura.setNebulosidade(20);
            leitura.setLuminosidade(18);
            leitura.calcularObservacao();
            leituraRepo.save(leitura);
        };
    }

    private Passagem novaPassagem(Satelite s, Localizacao l, String horario, String direcao, boolean visivel) {
        Passagem p = new Passagem();
        p.setSatelite(s);
        p.setLocalizacao(l);
        p.setHorario(horario);
        p.setDirecao(direcao);
        p.setVisivel(visivel);
        return p;
    }
}
