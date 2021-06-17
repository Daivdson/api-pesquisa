package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoGrupoItensAdjudicado {
	private String nomeFornecedor;
	private String cpfCnpjFornecedor;
	private List<LicitacaoItensAdjudicado> itens;
	private Double valorTotalFornecedor;

	@Builder
	public LicitacaoGrupoItensAdjudicado(String nomeFornecedor, String cpfCnpjFornecedor, List<LicitacaoItensAdjudicado> itens, Double valorTotalFornecedor) {
		this.nomeFornecedor =  nomeFornecedor;
		this.cpfCnpjFornecedor = cpfCnpjFornecedor;
		this.itens = itens;
		this.valorTotalFornecedor = valorTotalFornecedor;
	}
}
