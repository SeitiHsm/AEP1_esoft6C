package AEP.ClothIn.repositories;

import AEP.ClothIn.enums.StatusDoacao;
import AEP.ClothIn.models.Doacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoacaoRepository extends MongoRepository<Doacao, String> {

    List<Doacao> findByDoadorId(String doadorId);

    List<Doacao> findByResponsavelId(String responsavelId);

    List<Doacao> findByStatus(StatusDoacao status);
}
