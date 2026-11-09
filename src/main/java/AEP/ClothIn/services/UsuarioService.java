package AEP.ClothIn.services;

import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Usuario;
import AEP.ClothIn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criar(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado!");
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean verificarTipo(String id, TipoUsuario tipoUsuario) {
        return usuarioRepository.existsByIdAndTipoUsuario(id, tipoUsuario);
    }

    public Usuario atualizar(String id, Usuario usuarioAtualizado) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())
                && usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado!");
        }

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        usuarioExistente.setTipoUsuario(usuarioAtualizado.getTipoUsuario());

        return usuarioRepository.save(usuarioExistente);
    }

    public void deletar(String id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        usuarioRepository.deleteById(id);
    }
}
