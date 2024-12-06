package br.com.portfoliopelusci.model.documento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sigla", "versao"})
})
public class Documento {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private int versao;
    private String sigla;

    @Enumerated(EnumType.STRING)
    private FaseEnum fase;
    
    
    

	public Documento() {
		super();
	}

	public Documento(Long id, String titulo, String descricao, int versao, String sigla, FaseEnum fase) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.versao = versao;
		this.sigla = sigla;
		this.fase = fase;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public FaseEnum getFase() {
		return fase;
	}

	public void setFase(FaseEnum fase) {
		this.fase = fase;
	}
    
    


}