package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoItensAdjudicado {
	
	private Integer id;
	private double quantidade;
	private double valorUnitario;
	private double valorItem;
	private String descricao;
	private List<LicitacaoSubItensAdjudicado> subItens;
	private String unidadeItem;

	@Builder
	public LicitacaoItensAdjudicado(Integer id, double quantidade, double valorUnitario, double valorItem, String descricao, List<LicitacaoSubItensAdjudicado> subItens, String unidadeItem) {
		this.id = id;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.valorItem = valorItem;
		this.descricao = descricao;
		this.subItens = subItens;
		this.unidadeItem = unidadeItem;
	}
}
