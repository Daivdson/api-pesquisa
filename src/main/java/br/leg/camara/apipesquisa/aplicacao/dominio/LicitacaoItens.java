package br.leg.camara.apipesquisa.aplicacao.dominio;

import static org.apache.commons.lang3.StringUtils.trim;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LicitacaoItens {

	private Integer id;
	private Integer numero;
	private String descricao;
	private Integer quantidade;
	private Double valorUnitario;
	private Double valorItem;
	private String situacao;
	private String unidade;
	private Integer idAtaRegistroPreco;
	private Integer numeroAtaRegistroPreco;
	private Integer anoAtaRegistroPreco;
	private List<LicitacaoSubItens> subItens;

	@Builder
	public LicitacaoItens(Integer id, Integer numero, String descricao, Integer quantidade, Double valorItem, String situacao, String unidade, Integer idAtaRegistroPreco, Integer numeroAtaRegistroPreco,
			Integer anoAtaRegistroPreco, List<LicitacaoSubItens> subItens, Double valorUnitatio) {		
		this.id = id;
		this.numero = numero;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.valorItem = valorItem;
		this.situacao = situacao;
		this.unidade = trim(unidade);
		this.idAtaRegistroPreco = idAtaRegistroPreco;
		this.numeroAtaRegistroPreco = numeroAtaRegistroPreco;
		this.anoAtaRegistroPreco = anoAtaRegistroPreco;
		this.subItens = subItens;
		this.valorUnitario = valorUnitatio;
	}

}
