package com.fiap.satellitetracker.service;

import com.fiap.satellitetracker.dto.AlertaRequest;
import com.fiap.satellitetracker.dto.LeituraSensorRequest;
import com.fiap.satellitetracker.dto.LoginRequest;
import com.fiap.satellitetracker.dto.UsuarioRequest;
import com.fiap.satellitetracker.dto.UsuarioResponse;
import com.fiap.satellitetracker.exception.NegocioException;
import com.fiap.satellitetracker.model.Alerta;
import com.fiap.satellitetracker.model.LeituraSensor;
import com.fiap.satellitetracker.model.Localizacao;
import com.fiap.satellitetracker.model.Usuario;
import com.fiap.satellitetracker.repository.AlertaRepository;
import com.fiap.satellitetracker.repository.LeituraSensorRepository;
import com.fiap.satellitetracker.repository.LocalizacaoRepository;
import com.fiap.satellitetracker.repository.UsuarioRepository;
import com.fiap.satellitetracker.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ServiceScenarioTest {

    @Test
    void deveCriarUsuarioComSenhaCriptografada() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = new JwtService();
        UsuarioService service = new UsuarioService(usuarioRepository, passwordEncoder, jwtService);
        UsuarioRequest request = usuarioRequest("Ana Silva", "ana@email.com", "senha123");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(10L);
            return usuario;
        });

        UsuarioResponse response = service.criar(request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNome()).isEqualTo("Ana Silva");
        assertThat(response.getEmail()).isEqualTo("ana@email.com");

        ArgumentCaptor<Usuario> usuarioSalvo = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioSalvo.capture());
        assertThat(usuarioSalvo.getValue().getSenhaHash()).isEqualTo("hash-bcrypt");
    }

    @Test
    void naoDeveCriarUsuarioComEmailDuplicado() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = new JwtService();
        UsuarioService service = new UsuarioService(usuarioRepository, passwordEncoder, jwtService);
        UsuarioRequest request = usuarioRequest("Ana Silva", "ana@email.com", "senha123");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(NegocioException.class)
                .hasMessage("Ja existe um usuario com este email");

        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void naoDeveLogarComSenhaInvalida() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = new JwtService();
        UsuarioService service = new UsuarioService(usuarioRepository, passwordEncoder, jwtService);
        Usuario usuario = usuario(1L, "Ana Silva", "ana@email.com", "hash-bcrypt");
        LoginRequest request = loginRequest("ana@email.com", "senha-errada");

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-errada", "hash-bcrypt")).thenReturn(false);

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(NegocioException.class)
                .hasMessage("Email ou senha invalidos");

        verify(passwordEncoder).matches("senha-errada", "hash-bcrypt");
    }

    @Test
    void deveCriarAlertaAtivoPorPadrao() {
        AlertaRepository alertaRepository = mock(AlertaRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AlertaService service = new AlertaService(alertaRepository, usuarioRepository);
        Usuario usuario = usuario(7L, "Ana Silva", "ana@email.com", "hash-bcrypt");
        AlertaRequest request = new AlertaRequest();
        request.setUsuarioId(7L);
        request.setMensagem("Passagem visivel em breve");

        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alerta alerta = service.criar(request);

        assertThat(alerta.getUsuario()).isSameAs(usuario);
        assertThat(alerta.getMensagem()).isEqualTo("Passagem visivel em breve");
        assertThat(alerta.getAtivo()).isTrue();
    }

    @Test
    void deveRegistrarLeituraFavoravelQuandoCondicoesPermitiremObservacao() {
        LeituraSensorRepository leituraRepository = mock(LeituraSensorRepository.class);
        LocalizacaoRepository localizacaoRepository = mock(LocalizacaoRepository.class);
        LeituraSensorService service = new LeituraSensorService(leituraRepository, localizacaoRepository);
        Localizacao localizacao = new Localizacao();
        localizacao.setId(3L);
        LeituraSensorRequest request = leituraSensorRequest(3L, 21, false, 20, 30);

        when(localizacaoRepository.findById(3L)).thenReturn(Optional.of(localizacao));
        when(leituraRepository.save(any(LeituraSensor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LeituraSensor leitura = service.registrar(request);

        assertThat(leitura.getLocalizacao()).isSameAs(localizacao);
        assertThat(leitura.getTemperatura()).isEqualTo(21);
        assertThat(leitura.getChovendo()).isFalse();
        assertThat(leitura.getNebulosidade()).isEqualTo(20);
        assertThat(leitura.getLuminosidade()).isEqualTo(30);
        assertThat(leitura.getObservacaoFavoravel()).isTrue();
    }

    private UsuarioRequest usuarioRequest(String nome, String email, String senha) {
        UsuarioRequest request = new UsuarioRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        return request;
    }

    private LoginRequest loginRequest(String email, String senha) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setSenha(senha);
        return request;
    }

    private LeituraSensorRequest leituraSensorRequest(Long localizacaoId,
                                                      Integer temperatura,
                                                      Boolean chovendo,
                                                      Integer nebulosidade,
                                                      Integer luminosidade) {
        LeituraSensorRequest request = new LeituraSensorRequest();
        request.setLocalizacaoId(localizacaoId);
        request.setTemperatura(temperatura);
        request.setChovendo(chovendo);
        request.setNebulosidade(nebulosidade);
        request.setLuminosidade(luminosidade);
        return request;
    }

    private Usuario usuario(Long id, String nome, String email, String senhaHash) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenhaHash(senhaHash);
        return usuario;
    }
}
