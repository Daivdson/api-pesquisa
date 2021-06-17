package br.leg.camara.apipesquisa.aplicacao.dominio;

import static org.apache.commons.lang3.StringUtils.trim;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoSubItens {

	private Integer id;
	private Integer numero;
	private String situacao;
	private String descricao;
	private String unidade;
	private Double quantidade;
	private Double valorItem;
	private Double valorUnitario;

	@Builder
	public LicitacaoSubItens(Integer id, Integer numero, String situacao, String descricao, String unidade, Double quantidade, 
			Double valorItem, Double valorUnitario) {
		this.id = id;
		this.numero = numero;
		this.situacao = situacao;
		this.descricao = descricao;
		this.unidade = trim(unidade);
		this.quantidade = quantidade;
		this.valorItem = valorItem;
		this.valorUnitario = valorUnitario;
	}

}
