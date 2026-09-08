package AEP.ClothIn;

import AEP.ClothIn.models.Roupa;
import AEP.ClothIn.repositories.RoupaRepository;
import AEP.ClothIn.services.RoupaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	void deveBuscarRoupaPorId() {
		Roupa roupa = new Roupa("1", "Calça", "38", "Preta", "Usado");
		when(roupaRepository.findById("1")).thenReturn(Optional.of(roupa));

		Optional<Roupa> resultado = roupaService.buscarPorId("1");

		assertTrue(resultado.isPresent());
		assertEquals("Calça", resultado.get().getTipo());
	}

	@Test
	void deveRetornarFalseAoDeletarRoupaInexistente() {
		when(roupaRepository.existsById("99")).thenReturn(false);

		boolean deletado = roupaService.deletar("99");

		assertFalse(deletado);
		verify(roupaRepository, never()).deleteById("99");
	}
}