package AEP.ClothIn;

import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Usuario;
import AEP.ClothIn.repositories.UsuarioRepository;
import AEP.ClothIn.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario criarUsuarioExemplo(String id, String email) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNome("João Silva");
        u.setEmail(email);
        u.setTelefone("44999999999");
        u.setTipoUsuario(TipoUsuario.DOADOR);
        return u;
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        Usuario usuario = criarUsuarioExemplo("1", "joao@email.com");
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario criado = usuarioService.criar(usuario);

        assertNotNull(criado);
        assertEquals("joao@email.com", criado.getEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComEmailDuplicado() {
        Usuario usuario = criarUsuarioExemplo("1", "joao@email.com");
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.criar(usuario));

        assertEquals("Este e-mail já está cadastrado!", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveListarTodosOsUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(criarUsuarioExemplo("1", "joao@email.com")));

        List<Usuario> lista = usuarioService.listarTodos();

        assertEquals(1, lista.size());
    }

    @Test
    void deveBuscarUsuarioPorIdEEmail() {
        Usuario usuario = criarUsuarioExemplo("1", "joao@email.com");
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

        assertTrue(usuarioService.buscarPorId("1").isPresent());
        assertTrue(usuarioService.buscarPorEmail("joao@email.com").isPresent());
    }

    @Test
    void deveVerificarTipoUsuario() {
        when(usuarioRepository.existsByIdAndTipoUsuario("1", TipoUsuario.DOADOR)).thenReturn(true);

        assertTrue(usuarioService.verificarTipo("1", TipoUsuario.DOADOR));
    }

    @Test
    void deveAtualizarUsuarioComSucessoMesmoEmail() {
        Usuario existente = criarUsuarioExemplo("1", "joao@email.com");
        Usuario atualizado = criarUsuarioExemplo("1", "joao@email.com");
        atualizado.setNome("João Atualizado");

        when(usuarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        Usuario resultado = usuarioService.atualizar("1", atualizado);

        assertEquals("João Atualizado", resultado.getNome());
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void deveAtualizarUsuarioComNovoEmailDisponivel() {
        Usuario existente = criarUsuarioExemplo("1", "joao@email.com");
        Usuario atualizado = criarUsuarioExemplo("1", "novo@email.com");

        when(usuarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("novo@email.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        Usuario resultado = usuarioService.atualizar("1", atualizado);

        assertEquals("novo@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        Usuario atualizado = criarUsuarioExemplo("99", "joao@email.com");
        when(usuarioRepository.findById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.atualizar("99", atualizado));

        assertEquals("Usuário não encontrado!", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoAoAtualizarParaEmailJaCadastrado() {
        Usuario existente = criarUsuarioExemplo("1", "joao@email.com");
        Usuario atualizado = criarUsuarioExemplo("1", "outro@email.com");

        when(usuarioRepository.findById("1")).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmail("outro@email.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.atualizar("1", atualizado));

        assertEquals("Este e-mail já está cadastrado!", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.existsById("1")).thenReturn(true);

        usuarioService.deletar("1");

        verify(usuarioRepository, times(1)).deleteById("1");
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(usuarioRepository.existsById("99")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.deletar("99"));

        assertEquals("Usuário não encontrado!", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(any());
    }
}
