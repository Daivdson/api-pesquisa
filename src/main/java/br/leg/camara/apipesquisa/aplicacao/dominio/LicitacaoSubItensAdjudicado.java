package br.leg.camara.apipesquisa.aplicacao.dominio;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoSubItensAdjudicado {

	private Integer id;
	private Double quantidade;
	private Double valorUnitario;
	private Double valorItem;
	private String descricao;
	private String unidadeSubItem;

	@Builder
	public LicitacaoSubItensAdjudicado(Integer id, Double quantidade, Double valorUnitario, Double valorItem, String descricao, String unidadeSubItem) {		
		this.id = id;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.valorItem = valorItem;
		this.descricao = descricao;
		this.unidadeSubItem = unidadeSubItem;
	}
}
