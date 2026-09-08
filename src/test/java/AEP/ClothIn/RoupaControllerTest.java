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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
    void deveRetornarStatus404QuandoBuscarIdInexistente() throws Exception {
        when(roupaService.buscarPorId("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/roupas/99"))
                .andExpect(status().isNotFound());
    }
}