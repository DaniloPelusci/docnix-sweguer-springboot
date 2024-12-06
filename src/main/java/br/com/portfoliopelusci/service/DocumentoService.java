package br.com.portfoliopelusci.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.portfoliopelusci.exceptions.ResourceNotFoundException;
import br.com.portfoliopelusci.model.documento.Documento;
import br.com.portfoliopelusci.model.documento.FaseEnum;
import br.com.portfoliopelusci.model.dto.DocumentoDTO;
import br.com.portfoliopelusci.repository.DocumentoRepository;

@Service
public class DocumentoService {
    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento criarDocumento(Documento documento) {
        documento.setFase(FaseEnum.MINUTA);
        documento.setVersao(1);
        if(documentoRepository.findBySiglaAndVersao(documento.getSigla(), documento.getVersao()).isPresent()) {
        	throw new ResourceNotFoundException("A sigla " +documento.getSigla()+" com a versão"+documento.getVersao()+" ja existe.");
        }
        return documentoRepository.save(documento);
    }
    
    public Documento editarDocumento(DocumentoDTO documento) {
    	Documento docEditar = documentoRepository.findById(documento.getId()).get();
        
    	docEditar.setDescricao(documento.getDescricao());
    	docEditar.setTitulo(documento.getTitulo());
        
        return documentoRepository.save(docEditar);
    }
  

    public Documento submeterDocumento(Long id) {
        Documento documento = documentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado"));
        
        if (!documento.getFase().equals(FaseEnum.MINUTA)) {
            throw new ResourceNotFoundException("Apenas documentos na fase MINUTA podem ser submetidos");
        }
   
        documento.setFase(FaseEnum.VIGENTE);
        documentoRepository.findBySiglaAndFase(documento.getSigla(), FaseEnum.VIGENTE)
            .forEach(doc -> {
                doc.setFase(FaseEnum.OBSOLETO);
                documentoRepository.save(doc);
            });
        return documentoRepository.save(documento);
    }

    public Documento criarNovaVersao(Long id) {
        Documento vigente = documentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado"));
        if (!vigente.getFase().equals(FaseEnum.VIGENTE)) {
            throw new ResourceNotFoundException("Apenas documentos VIGENTES podem gerar novas versões");
        }
     
        Documento novaVersao = new Documento();
        novaVersao.setTitulo(vigente.getTitulo());
        novaVersao.setDescricao(vigente.getDescricao());
        novaVersao.setSigla(vigente.getSigla());
        novaVersao.setFase(FaseEnum.MINUTA);
        novaVersao.setVersao(vigente.getVersao() + 1);
        vigente.setFase(FaseEnum.OBSOLETO);
        documentoRepository.save(vigente);
        return documentoRepository.save(novaVersao);
    }

	public List<Documento> listarTodos() {
		 return documentoRepository.findAll();
	}



	public Documento buscarPorId(Long id) {
		return documentoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado com ID: " + id));
	}
	
	 public Documento obsoletarDocumento(Long id) {
	        Documento documento = buscarPorId(id);

	        if (!FaseEnum.VIGENTE.equals(documento.getFase())) {
	            throw new ResourceNotFoundException("Somente documentos na fase VIGENTE podem ser obsoletados.");
	        }

	        documento.setFase(FaseEnum.OBSOLETO);
	        return documentoRepository.save(documento);
	    }
}
