package AEP.ClothIn.repositories;

import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByIdAndTipoUsuario(String id, TipoUsuario tipoUsuario);
}
