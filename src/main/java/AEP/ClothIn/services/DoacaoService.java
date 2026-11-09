package AEP.ClothIn.services;

import AEP.ClothIn.enums.StatusDoacao;
import AEP.ClothIn.enums.TipoUsuario;
import AEP.ClothIn.models.Doacao;
import AEP.ClothIn.repositories.DoacaoRepository;
import AEP.ClothIn.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public DoacaoService(
            DoacaoRepository doacaoRepository,
            UsuarioRepository usuarioRepository) {

        this.doacaoRepository = doacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Doacao criar(Doacao doacao) {

        if (!usuarioRepository.existsByIdAndTipoUsuario(
                doacao.getDoadorId(), TipoUsuario.DOADOR)) {
            throw new RuntimeException("Doador não encontrado ou inválido!");
        }

        if (doacao.getResponsavelId() != null &&
                !usuarioRepository.existsByIdAndTipoUsuario(
                        doacao.getResponsavelId(), TipoUsuario.RESPONSAVEL)) {
            throw new RuntimeException("Responsável não encontrado ou inválido!");
        }

        if (doacao.getRoupas() == null || doacao.getRoupas().isEmpty()) {
            throw new RuntimeException("A doação precisa conter pelo menos uma roupa!");
        }

        doacao.setId(null);
        doacao.setData(LocalDate.now());
        doacao.setStatus(StatusDoacao.PENDENTE);

        return doacaoRepository.save(doacao);
    }

    public List<Doacao> listarTodas() {
        return doacaoRepository.findAll();
    }

    public Optional<Doacao> buscarPorId(String id) {
        return doacaoRepository.findById(id);
    }

    public List<Doacao> buscarPorDoador(String doadorId) {
        return doacaoRepository.findByDoadorId(doadorId);
    }

    public List<Doacao> buscarPorResponsavel(String responsavelId) {
        return doacaoRepository.findByResponsavelId(responsavelId);
    }

    public List<Doacao> buscarPorStatus(StatusDoacao status) {
        return doacaoRepository.findByStatus(status);
    }

    public Doacao atualizar(String id, Doacao doacaoAtualizada) {

        Doacao doacaoExistente = doacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada!"));

        if (!usuarioRepository.existsByIdAndTipoUsuario(
                doacaoAtualizada.getDoadorId(), TipoUsuario.DOADOR)) {
            throw new RuntimeException("Doador não encontrado ou inválido!");
        }

        if (doacaoAtualizada.getResponsavelId() != null &&
                !usuarioRepository.existsByIdAndTipoUsuario(
                        doacaoAtualizada.getResponsavelId(), TipoUsuario.RESPONSAVEL)) {
            throw new RuntimeException("Responsável não encontrado ou inválido!");
        }

        if (doacaoAtualizada.getRoupas() == null ||
                doacaoAtualizada.getRoupas().isEmpty()) {
            throw new RuntimeException("A doação precisa conter pelo menos uma roupa!");
        }

        doacaoExistente.setDoadorId(doacaoAtualizada.getDoadorId());
        doacaoExistente.setResponsavelId(doacaoAtualizada.getResponsavelId());
        doacaoExistente.setStatus(doacaoAtualizada.getStatus());
        doacaoExistente.setRoupas(doacaoAtualizada.getRoupas());

        return doacaoRepository.save(doacaoExistente);
    }

    public void deletar(String id) {

        if (!doacaoRepository.existsById(id)) {
            throw new RuntimeException("Doação não encontrada!");
        }

        doacaoRepository.deleteById(id);
    }
}
