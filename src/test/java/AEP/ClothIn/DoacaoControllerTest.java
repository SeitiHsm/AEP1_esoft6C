package AEP.ClothIn;

import AEP.ClothIn.controllers.DoacaoController;
import AEP.ClothIn.enums.StatusDoacao;
import AEP.ClothIn.models.Doacao;
import AEP.ClothIn.models.RoupaDoacao;
import AEP.ClothIn.services.DoacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoacaoController.class)
class DoacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoacaoService doacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nonnull
    private Doacao criarDoacaoExemplo() {
        Doacao d = new Doacao();
        d.setId("10");
        d.setDoadorId("doador1");
        d.setResponsavelId("resp1");
        d.setStatus(StatusDoacao.PENDENTE);
        d.setRoupas(List.of(new RoupaDoacao()));
        return d;
    }

    @Test
    void deveCriarDoacaoERetornarStatus201() throws Exception {
        Doacao doacao = criarDoacaoExemplo();
        when(doacaoService.criar(any(Doacao.class))).thenReturn(doacao);

        mockMvc.perform(post("/doacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doacao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.doadorId").value("doador1"));
    }

    @Test
    void deveListarTodasAsDoacoesERetornarStatus200() throws Exception {
        when(doacaoService.listarTodas()).thenReturn(List.of(criarDoacaoExemplo()));

        mockMvc.perform(get("/doacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("10"));
    }

    @Test
    void deveBuscarDoacaoPorIdERetornarStatus200() throws Exception {
        when(doacaoService.buscarPorId("10")).thenReturn(Optional.of(criarDoacaoExemplo()));

        mockMvc.perform(get("/doacoes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"));
    }

    @Test
    void deveLancarExcecaoAoBuscarDoacaoInexistente() {
        when(doacaoService.buscarPorId("99")).thenReturn(Optional.empty());

        assertThrows(ServletException.class, () -> mockMvc.perform(get("/doacoes/99")));
    }

    @Test
    void deveBuscarPorDoadorResponsavelEStatus() throws Exception {
        List<Doacao> lista = List.of(criarDoacaoExemplo());
        when(doacaoService.buscarPorDoador("doador1")).thenReturn(lista);
        when(doacaoService.buscarPorResponsavel("resp1")).thenReturn(lista);
        when(doacaoService.buscarPorStatus(StatusDoacao.PENDENTE)).thenReturn(lista);

        mockMvc.perform(get("/doacoes/doador/doador1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doadorId").value("doador1"));

        mockMvc.perform(get("/doacoes/responsavel/resp1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].responsavelId").value("resp1"));

        mockMvc.perform(get("/doacoes/status/PENDENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDENTE"));
    }

    @Test
    void deveAtualizarDoacaoERetornarStatus200() throws Exception {
        Doacao doacao = criarDoacaoExemplo();
        when(doacaoService.atualizar(eq("10"), any(Doacao.class))).thenReturn(doacao);

        mockMvc.perform(put("/doacoes/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"));
    }

    @Test
    void deveDeletarDoacaoERetornarStatus204() throws Exception {
        doNothing().when(doacaoService).deletar("10");

        mockMvc.perform(delete("/doacoes/10"))
                .andExpect(status().isNoContent());
    }
}
