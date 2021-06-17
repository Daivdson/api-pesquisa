package br.leg.camara.apipesquisa.adaptadores.controlador.dto;

import java.util.List;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.enums.Uf;
import lombok.Data;

@Data
public class ParametroCotasDTO {
	
	private Integer pagina;
	private Integer quantidade;
	private TipoOrdenacao ordenacao = TipoOrdenacao.DESC;
	private String geral;
	private String mesAno;
	private String mesAnoInicial;
	private String mesAnoFinal;
	private String siglaPartido;
	private String tipoDespesa;
	private Uf uf;
	private boolean pesquisaPorDeputado;
	private boolean pesquisaPorLideranca;
	private List<String> deputado;
	private List<String> lideranca;
}
