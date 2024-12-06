package br.com.portfoliopelusci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.portfoliopelusci.model.documento.Documento;
import br.com.portfoliopelusci.model.documento.FaseEnum;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    Optional<Documento> findBySiglaAndVersao(String sigla, int versao);
    List<Documento> findBySiglaAndFase(String sigla, FaseEnum fase);
}