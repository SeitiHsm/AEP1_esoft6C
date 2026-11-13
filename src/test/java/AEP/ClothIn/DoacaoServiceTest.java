package AEP.ClothIn;

import AEP.ClothIn.enums.StatusDoacao;
import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Doacao;
import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.models.RoupaDoacao;
import AEP.ClothIn.repositories.DoacaoRepository;
import AEP.ClothIn.repositories.UsuarioRepository;
import AEP.ClothIn.services.DoacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoacaoServiceTest {

    @Mock
    private DoacaoRepository doacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DoacaoService doacaoService;

    private Doacao criarDoacaoValida(String responsavelId) {
        Doacao d = new Doacao();
        d.setId("10");
        d.setDoadorId("doador1");
        d.setResponsavelId(responsavelId);
        d.setStatus(StatusDoacao.PENDENTE);
        d.setRoupas(List.of(new RoupaDoacao()));
        return d;
    }

    @Test
    void deveCriarDoacaoComResponsavelValido() {
        Doacao doacao = criarDoacaoValida("resp1");
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(usuarioRepository.existsByIdAndTipoUsuario("resp1", TipoUsuario.RESPONSAVEL)).thenReturn(true);
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao salva = doacaoService.criar(doacao);

        assertNull(salva.getId());
        assertNotNull(salva.getData());
        assertEquals(StatusDoacao.PENDENTE, salva.getStatus());
        verify(doacaoRepository, times(1)).save(doacao);
    }

    @Test
    void deveCriarDoacaoSemResponsavel() {
        Doacao doacao = criarDoacaoValida(null);
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Doacao salva = doacaoService.criar(doacao);

        assertEquals(StatusDoacao.PENDENTE, salva.getStatus());
        verify(usuarioRepository, never()).existsByIdAndTipoUsuario(any(), eq(TipoUsuario.RESPONSAVEL));
    }

    @Test
    void deveLancarExcecaoAoCriarComDoadorInvalido() {
        Doacao doacao = criarDoacaoValida(null);
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> doacaoService.criar(doacao));
        assertEquals("Doador não encontrado ou inválido!", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoAoCriarComResponsavelInvalido() {
        Doacao doacao = criarDoacaoValida("respInvalido");
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(usuarioRepository.existsByIdAndTipoUsuario("respInvalido", TipoUsuario.RESPONSAVEL)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> doacaoService.criar(doacao));
        assertEquals("Responsável não encontrado ou inválido!", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoAoCriarSemRoupasOuListaVazia() {
        Doacao semLista = criarDoacaoValida(null);
        semLista.setRoupas(null);

        Doacao listaVazia = criarDoacaoValida(null);
        listaVazia.setRoupas(Collections.emptyList());

        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> doacaoService.criar(semLista));
        assertThrows(RuntimeException.class, () -> doacaoService.criar(listaVazia));
    }

    @Test
    void deveExecutarConsultasDeDoacao() {
        Doacao d = criarDoacaoValida("resp1");
        when(doacaoRepository.findAll()).thenReturn(List.of(d));
        when(doacaoRepository.findById("10")).thenReturn(Optional.of(d));
        when(doacaoRepository.findByDoadorId("doador1")).thenReturn(List.of(d));
        when(doacaoRepository.findByResponsavelId("resp1")).thenReturn(List.of(d));
        when(doacaoRepository.findByStatus(StatusDoacao.PENDENTE)).thenReturn(List.of(d));

        assertEquals(1, doacaoService.listarTodas().size());
        assertTrue(doacaoService.buscarPorId("10").isPresent());
        assertEquals(1, doacaoService.buscarPorDoador("doador1").size());
        assertEquals(1, doacaoService.buscarPorResponsavel("resp1").size());
        assertEquals(1, doacaoService.buscarPorStatus(StatusDoacao.PENDENTE).size());
    }

    @Test
    void deveAtualizarDoacaoComSucesso() {
        Doacao existente = criarDoacaoValida(null);
        Doacao atualizada = criarDoacaoValida("resp1");
        atualizada.setStatus(StatusDoacao.CONCLUIDA);

        when(doacaoRepository.findById("10")).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(usuarioRepository.existsByIdAndTipoUsuario("resp1", TipoUsuario.RESPONSAVEL)).thenReturn(true);
        when(doacaoRepository.save(any(Doacao.class))).thenReturn(existente);

        Doacao resultado = doacaoService.atualizar("10", atualizada);

        assertEquals("resp1", resultado.getResponsavelId());
        assertEquals(StatusDoacao.CONCLUIDA, resultado.getStatus());
    }

    @Test
    void deveAtualizarDoacaoSemResponsavel() {
        Doacao existente = criarDoacaoValida("resp1");
        Doacao atualizada = criarDoacaoValida(null);

        when(doacaoRepository.findById("10")).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(doacaoRepository.save(any(Doacao.class))).thenReturn(existente);

        Doacao resultado = doacaoService.atualizar("10", atualizada);

        assertNull(resultado.getResponsavelId());
    }

    @Test
    void deveLancarExcecoesNosCenariosDeErroAoAtualizar() {
        Doacao atualizada = criarDoacaoValida("resp1");

        // 1. Doação inexistente
        when(doacaoRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> doacaoService.atualizar("99", atualizada));

        // 2. Doador inválido
        when(doacaoRepository.findById("10")).thenReturn(Optional.of(criarDoacaoValida(null)));
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> doacaoService.atualizar("10", atualizada));

        // 3. Responsável inválido
        when(usuarioRepository.existsByIdAndTipoUsuario("doador1", TipoUsuario.DOADOR)).thenReturn(true);
        when(usuarioRepository.existsByIdAndTipoUsuario("resp1", TipoUsuario.RESPONSAVEL)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> doacaoService.atualizar("10", atualizada));

        // 4. Roupas nulas ou vazias
        when(usuarioRepository.existsByIdAndTipoUsuario("resp1", TipoUsuario.RESPONSAVEL)).thenReturn(true);
        atualizada.setRoupas(null);
        assertThrows(RuntimeException.class, () -> doacaoService.atualizar("10", atualizada));

        atualizada.setRoupas(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> doacaoService.atualizar("10", atualizada));
    }

    @Test
    void deveDeletarDoacaoComSucesso() {
        when(doacaoRepository.existsById("10")).thenReturn(true);

        doacaoService.deletar("10");

        verify(doacaoRepository, times(1)).deleteById("10");
    }

    @Test
    void deveLancarExcecaoAoDeletarDoacaoInexistente() {
        when(doacaoRepository.existsById("99")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> doacaoService.deletar("99"));
        assertEquals("Doação não encontrada!", ex.getMessage());
    }
}
