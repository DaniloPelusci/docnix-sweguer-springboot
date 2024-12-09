package br.com.portfoliopelusci.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.portfoliopelusci.model.documento.Documento;
import br.com.portfoliopelusci.model.dto.DocumentoDTO;
import br.com.portfoliopelusci.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/documentos")
public class DocumentoController {
    @Autowired
    private DocumentoService service;

    @Operation(summary = "Listar todos os documentos", description = "Retorna uma lista de todos os documentos cadastrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de documentos retornada com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class)))
    })
    @GetMapping
    public ResponseEntity<List<Documento>> listarTodos() {
        List<Documento> documentos = service.listarTodos();
        return ResponseEntity.ok(documentos);
    }
    
    @Operation(summary = "Buscar documento por ID", description = "Retorna os detalhes de um documento específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento encontrado com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class))),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Documento> buscarPorId(@PathVariable Long id) {
        Documento documento = service.buscarPorId(id);
        return ResponseEntity.ok(documento);
    }
    
    @Operation(summary = "Criar um novo documento", description = "Cria um novo documento no sistema com a fase inicial MINUTA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documento criado com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class)))
    })
    @PostMapping
    public ResponseEntity<Documento> criarDocumento(@RequestBody Documento documento) {
        Documento novoDocumento = service.criarDocumento(documento);
        return ResponseEntity.status(201).body(novoDocumento);
    }
    
    @Operation(summary = "Submeter um documento", description = "Submete um documento da fase MINUTA para a fase VIGENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento submetido com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class))),
        @ApiResponse(responseCode = "400", description = "Erro ao submeter o documento")
    })
    @PutMapping("/{id}/submeter")
    public ResponseEntity<Documento> submeterDocumento(@PathVariable Long id) {
        Documento documentoAtualizado = service.submeterDocumento(id);
        return ResponseEntity.ok(documentoAtualizado);
    }
    
    @Operation(summary = "Obsoletar um documento", description = "Define a fase de um documento VIGENTE como OBSOLETO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento obsoletado com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class))),
        @ApiResponse(responseCode = "400", description = "Erro ao obsoletar o documento")
    })
    @PutMapping("/{id}/obsoletar")
    public ResponseEntity<Documento> obsoletarDocumento(@PathVariable Long id) {
        Documento documentoAtualizado = service.obsoletarDocumento(id);
        return ResponseEntity.ok(documentoAtualizado);
    }
    
    @Operation(summary = "Criar nova versão de um documento", description = "Cria uma nova versão de um documento na fase MINUTA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nova versão criada com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class))),
        @ApiResponse(responseCode = "400", description = "Erro ao criar nova versão")
    })
    @PostMapping("/{id}/nova-versao")
    public ResponseEntity<Documento> criarNovaVersao(@PathVariable Long id) {
        Documento novaVersao = service.criarNovaVersao(id);
        return ResponseEntity.ok(novaVersao);
    }
    
    @Operation(summary = "Edita documento", description = "Edita titulo e descricao de documento ja criado .")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nova versão criada com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Documento.class))),
        @ApiResponse(responseCode = "400", description = "Erro ao criar nova versão")
    })
    @PutMapping("/editarDocumento")
    public ResponseEntity<Documento> criarNovaVersao(@RequestBody DocumentoDTO doc) {
        Documento novaVersao = service.editarDocumento(doc);
        return ResponseEntity.ok(novaVersao);
    }
   

}