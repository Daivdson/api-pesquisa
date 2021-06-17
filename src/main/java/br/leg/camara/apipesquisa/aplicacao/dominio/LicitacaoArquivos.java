package br.leg.camara.apipesquisa.aplicacao.dominio;

import static org.apache.commons.lang3.StringUtils.trim;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoArquivos {

	private Integer id;
	private String nome;
	private String tipo;
	private String dataAtualizacao;

	@Builder
	public LicitacaoArquivos(Integer id, String nome, String tipo, String dataAtualizacao) {
		this.id = id;
		this.nome = nome;
		this.tipo = trim(tipo);
		this.dataAtualizacao = dataAtualizacao;
	}
}
