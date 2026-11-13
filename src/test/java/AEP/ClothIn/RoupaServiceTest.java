package AEP.ClothIn;

import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.repositories.RoupaRepository;
import AEP.ClothIn.services.RoupaService;
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
class RoupaServiceTest {

    @Mock
    private RoupaRepository roupaRepository;

    @InjectMocks
    private RoupaService roupaService;

    @Test
    void deveCriarRoupaComSucesso() {
        Roupa roupa = new Roupa("1", "Camisa", "M", "Azul", "Novo");
        when(roupaRepository.save(any(Roupa.class))).thenReturn(roupa);

        Roupa criada = roupaService.criar(roupa);

        assertNotNull(criada);
        assertEquals("Camisa", criada.getTipo());
        verify(roupaRepository, times(1)).save(roupa);
    }

    @Test
    void deveListarTodasAsRoupas() {
        List<Roupa> lista = List.of(
                new Roupa("1", "Camisa", "M", "Azul", "Novo"),
                new Roupa("2", "Calça", "40", "Preta", "Usado")
        );
        when(roupaRepository.findAll()).thenReturn(lista);

        List<Roupa> resultado = roupaService.listarTodas();

        assertEquals(2, resultado.size());
        verify(roupaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarRoupaPorId() {
        Roupa roupa = new Roupa("1", "Calça", "38", "Preta", "Usado");
        when(roupaRepository.findById("1")).thenReturn(Optional.of(roupa));

        Optional<Roupa> resultado = roupaService.buscarPorId("1");

        assertTrue(resultado.isPresent());
        assertEquals("Calça", resultado.get().getTipo());
    }

    @Test
    void deveAtualizarRoupaComSucesso() {
        Roupa existente = new Roupa("1", "Camisa", "M", "Azul", "Novo");
        Roupa atualizada = new Roupa("1", "Camisa Polo", "G", "Branca", "Seminovo");

        when(roupaRepository.findById("1")).thenReturn(Optional.of(existente));
        when(roupaRepository.save(any(Roupa.class))).thenReturn(existente);

        Roupa resultado = roupaService.atualizar("1", atualizada);

        assertNotNull(resultado);
        assertEquals("Camisa Polo", resultado.getTipo());
        assertEquals("G", resultado.getTamanho());
        assertEquals("Branca", resultado.getCor());
        assertEquals("Seminovo", resultado.getEstado());
        verify(roupaRepository, times(1)).save(existente);
    }

    @Test
    void deveRetornarNullAoAtualizarRoupaInexistente() {
        Roupa atualizada = new Roupa("99", "Camisa", "M", "Azul", "Novo");
        when(roupaRepository.findById("99")).thenReturn(Optional.empty());

        Roupa resultado = roupaService.atualizar("99", atualizada);

        assertNull(resultado);
        verify(roupaRepository, never()).save(any());
    }

    @Test
    void deveDeletarRoupaComSucesso() {
        when(roupaRepository.existsById("1")).thenReturn(true);

        boolean deletado = roupaService.deletar("1");

        assertTrue(deletado);
        verify(roupaRepository, times(1)).deleteById("1");
    }

    @Test
    void deveRetornarFalseAoDeletarRoupaInexistente() {
        when(roupaRepository.existsById("99")).thenReturn(false);

        boolean deletado = roupaService.deletar("99");

        assertFalse(deletado);
        verify(roupaRepository, never()).deleteById("99");
    }
}
