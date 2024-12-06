package br.com.portfoliopelusci;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.portfoliopelusci.exceptions.ResourceNotFoundException;
import br.com.portfoliopelusci.model.documento.Documento;
import br.com.portfoliopelusci.model.documento.FaseEnum;
import br.com.portfoliopelusci.repository.DocumentoRepository;
import br.com.portfoliopelusci.service.DocumentoService;

@ExtendWith(MockitoExtension.class)
class DocumentoServiceTest {

	@Mock
	private DocumentoRepository documentoRepository;

	@InjectMocks
	private DocumentoService documentoService;

	@Test
	void submeterDocumento_Sucesso() {
		Documento documento = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.MINUTA);

		when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));
		when(documentoRepository.save(any(Documento.class))).thenReturn(documento);

		Documento resultado = documentoService.submeterDocumento(1L);

		assertEquals(FaseEnum.VIGENTE, resultado.getFase());
		verify(documentoRepository, times(1)).save(documento);
	}

	@Test
	void submeterDocumento_FaseInvalida() {
		Documento documento = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.VIGENTE);

		when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> documentoService.submeterDocumento(1L));

		assertEquals("Apenas documentos na fase MINUTA podem ser submetidos", exception.getMessage());
	}
	
	@Test
	void obsoletarDocumento_Sucesso() {
	    Documento documento = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.VIGENTE);

	    when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));
	    when(documentoRepository.save(any(Documento.class))).thenReturn(documento);

	    Documento resultado = documentoService.obsoletarDocumento(1L);

	    assertEquals(FaseEnum.OBSOLETO, resultado.getFase());
	    verify(documentoRepository, times(1)).save(documento);
	}

	@Test
	void obsoletarDocumento_FaseInvalida() {
	    Documento documento = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.MINUTA);

	    when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
	        () -> documentoService.obsoletarDocumento(1L));

	    assertEquals("Somente documentos na fase VIGENTE podem ser obsoletados.", exception.getMessage());
	}
	
	@Test
	void criarDocumento_SiglaVersaoJaExistente() {
	    Documento documento = new Documento(null, "Título", "Descrição", 1, "SIG001", FaseEnum.MINUTA);

	    when(documentoRepository.findBySiglaAndVersao("SIG001", 1)).thenReturn(Optional.of(documento));

	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
	        () -> documentoService.criarDocumento(documento));

	    assertEquals("A sigla SIG001 com a versão1 ja existe.", exception.getMessage());
	}
	
	@Test
	void criarNovaVersao_Sucesso() {
	    Documento documentoVigente = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.VIGENTE);

	    when(documentoRepository.findById(1L)).thenReturn(Optional.of(documentoVigente));
	    when(documentoRepository.save(any(Documento.class))).thenAnswer(invocation -> invocation.getArgument(0));

	    Documento novaVersao = documentoService.criarNovaVersao(1L);

	    assertEquals(FaseEnum.MINUTA, novaVersao.getFase());
	    assertEquals(2, novaVersao.getVersao());
	    verify(documentoRepository, times(2)).save(any(Documento.class));
	}

	@Test
	void criarNovaVersao_NaoVigente() {
	    Documento documento = new Documento(1L, "Título", "Descrição", 1, "SIG001", FaseEnum.MINUTA);

	    when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
	        () -> documentoService.criarNovaVersao(1L));

	    assertEquals("Apenas documentos VIGENTES podem gerar novas versões", exception.getMessage());
	}

	@Test
	void criarNovaVersao_DocumentoNaoExistente() {
	    when(documentoRepository.findById(1L)).thenReturn(Optional.empty());

	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
	        () -> documentoService.criarNovaVersao(1L));

	    assertEquals("Documento não encontrado", exception.getMessage());
	}




}
