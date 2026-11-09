package AEP.ClothIn.controllers;

import AEP.ClothIn.enums.StatusDoacao;
import AEP.ClothIn.models.Doacao;
import AEP.ClothIn.services.DoacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doacao criar(@RequestBody Doacao doacao) {
        return doacaoService.criar(doacao);
    }

    @GetMapping
    public List<Doacao> listarTodas() {
        return doacaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public Doacao buscarPorId(@PathVariable String id) {
        return doacaoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada!"));
    }

    @GetMapping("/doador/{doadorId}")
    public List<Doacao> buscarPorDoador(@PathVariable String doadorId) {
        return doacaoService.buscarPorDoador(doadorId);
    }

    @GetMapping("/responsavel/{responsavelId}")
    public List<Doacao> buscarPorResponsavel(@PathVariable String responsavelId) {
        return doacaoService.buscarPorResponsavel(responsavelId);
    }

    @GetMapping("/status/{status}")
    public List<Doacao> buscarPorStatus(@PathVariable StatusDoacao status) {
        return doacaoService.buscarPorStatus(status);
    }

    @PutMapping("/{id}")
    public Doacao atualizar(
            @PathVariable String id,
            @RequestBody Doacao doacao) {
        return doacaoService.atualizar(id, doacao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable String id) {
        doacaoService.deletar(id);
    }
}
