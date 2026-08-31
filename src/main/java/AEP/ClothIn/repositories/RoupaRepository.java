package AEP.ClothIn.repositories;

import AEP.ClothIn.models.Roupa;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoupaRepository extends MongoRepository<Roupa, String> {
}
