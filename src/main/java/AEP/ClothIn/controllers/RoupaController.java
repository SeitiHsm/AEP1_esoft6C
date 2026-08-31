package AEP.ClothIn.controllers;

import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.services.RoupaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roupas")
public class RoupaController {

    private RoupaService roupaService;

    public RoupaController(RoupaService roupaService){
        this.roupaService = roupaService;
    }

    @PostMapping
    public ResponseEntity<Roupa> criar(@RequestBody Roupa roupa) {
        Roupa novaRoupa = roupaService.criar(roupa);
        return ResponseEntity.ok(novaRoupa);
    }

    @GetMapping
    public ResponseEntity<List<Roupa>> listarTodas() {
        return ResponseEntity.ok(roupaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Roupa> buscarPorId(@PathVariable String id) {
        return roupaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Roupa> atualizar(
            @PathVariable String id,
            @RequestBody Roupa roupa) {

        Roupa roupaAtualizada = roupaService.atualizar(id, roupa);

        if (roupaAtualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roupaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {

        if (!roupaService.deletar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
