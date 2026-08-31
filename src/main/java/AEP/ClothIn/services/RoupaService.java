package AEP.ClothIn.services;

import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.repositories.RoupaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoupaService {

    private RoupaRepository roupaRepository;

    public RoupaService(RoupaRepository roupaRepository) {
        this.roupaRepository = roupaRepository;
    }

    public Roupa criar(Roupa roupa) {
        return roupaRepository.save(roupa);
    }

    public List<Roupa> listarTodas() {
        return roupaRepository.findAll();
    }

    public Optional<Roupa> buscarPorId(String id) {
        return roupaRepository.findById(id);
    }

    public Roupa atualizar(String id, Roupa roupaAtualizada) {
        Optional<Roupa> roupaExistente = roupaRepository.findById(id);

        if (roupaExistente.isEmpty()) {
            return null;
        }

        Roupa roupa = roupaExistente.get();

        roupa.setTipo(roupaAtualizada.getTipo());
        roupa.setTamanho(roupaAtualizada.getTamanho());
        roupa.setCor(roupaAtualizada.getCor());
        roupa.setEstado(roupaAtualizada.getEstado());

        return roupaRepository.save(roupa);
    }

    public boolean deletar(String id) {
        if (!roupaRepository.existsById(id)) {
            return false;
        }

        roupaRepository.deleteById(id);
        return true;
    }
}
