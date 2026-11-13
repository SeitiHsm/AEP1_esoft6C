package AEP.ClothIn;

import AEP.ClothIn.controllers.RoupaController;
import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.services.RoupaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoupaController.class)
class RoupaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoupaService roupaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarRoupaERetornarStatus200() throws Exception {
        Roupa roupa = new Roupa("1", "Casaco", "G", "Preto", "Novo");
        when(roupaService.criar(any(Roupa.class))).thenReturn(roupa);

        mockMvc.perform(post("/roupas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roupa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.tipo").value("Casaco"));
    }

    @Test
    void deveListarTodasAsRoupasERetornarStatus200() throws Exception {
        List<Roupa> roupas = List.of(new Roupa("1", "Casaco", "G", "Preto", "Novo"));
        when(roupaService.listarTodas()).thenReturn(roupas);

        mockMvc.perform(get("/roupas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].tipo").value("Casaco"));
    }

    @Test
    void deveBuscarRoupaPorIdERetornarStatus200() throws Exception {
        Roupa roupa = new Roupa("1", "Casaco", "G", "Preto", "Novo");
        when(roupaService.buscarPorId("1")).thenReturn(Optional.of(roupa));

        mockMvc.perform(get("/roupas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void deveRetornarStatus404QuandoBuscarIdInexistente() throws Exception {
        when(roupaService.buscarPorId("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/roupas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarRoupaERetornarStatus200() throws Exception {
        Roupa roupa = new Roupa("1", "Jaqueta", "G", "Azul", "Usado");
        when(roupaService.atualizar(eq("1"), any(Roupa.class))).thenReturn(roupa);

        mockMvc.perform(put("/roupas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roupa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Jaqueta"));
    }

    @Test
    void deveRetornarStatus404AoAtualizarRoupaInexistente() throws Exception {
        Roupa roupa = new Roupa("99", "Jaqueta", "G", "Azul", "Usado");
        when(roupaService.atualizar(eq("99"), any(Roupa.class))).thenReturn(null);

        mockMvc.perform(put("/roupas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roupa)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarRoupaERetornarStatus204() throws Exception {
        when(roupaService.deletar("1")).thenReturn(true);

        mockMvc.perform(delete("/roupas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus404AoDeletarRoupaInexistente() throws Exception {
        when(roupaService.deletar("99")).thenReturn(false);

        mockMvc.perform(delete("/roupas/99"))
                .andExpect(status().isNotFound());
    }
}
