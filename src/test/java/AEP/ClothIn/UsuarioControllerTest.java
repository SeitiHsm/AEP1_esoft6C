package AEP.ClothIn;

import AEP.ClothIn.controllers.UsuarioController;
import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Usuario;
import AEP.ClothIn.services.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nonnull
    private Usuario criarUsuarioExemplo() {
        Usuario u = new Usuario();
        u.setId("1");
        u.setNome("Maria");
        u.setEmail("maria@email.com");
        u.setTelefone("44988887777");
        u.setTipoUsuario(TipoUsuario.DOADOR);
        return u;
    }

    @Test
    void deveCriarUsuarioERetornarStatus201() throws Exception {
        Usuario usuario = criarUsuarioExemplo();
        when(usuarioService.criar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveListarTodosUsuariosERetornarStatus200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(criarUsuarioExemplo()));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("maria@email.com"));
    }

    @Test
    void deveBuscarUsuarioPorIdERetornarStatus200() throws Exception {
        when(usuarioService.buscarPorId("1")).thenReturn(Optional.of(criarUsuarioExemplo()));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void deveLancarExcecaoQuandoBuscarUsuarioInexistente() {
        when(usuarioService.buscarPorId("99")).thenReturn(Optional.empty());

        assertThrows(ServletException.class, () -> mockMvc.perform(get("/usuarios/99")));
    }

    @Test
    void deveAtualizarUsuarioERetornarStatus200() throws Exception {
        Usuario usuario = criarUsuarioExemplo();
        when(usuarioService.atualizar(eq("1"), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveDeletarUsuarioERetornarStatus204() throws Exception {
        doNothing().when(usuarioService).deletar("1");

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
