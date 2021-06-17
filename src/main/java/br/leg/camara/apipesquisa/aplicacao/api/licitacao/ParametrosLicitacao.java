package br.leg.camara.apipesquisa.aplicacao.api.licitacao;

import java.util.List;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import lombok.Builder;
import lombok.Data;

@Data

public class ParametrosLicitacao {

	private String geral;
	private String situacao;
	private List<String> modalidade;
	private String dataInicio;
	private String dataFim;
	private Double valor;
	private Integer ano;
	private Integer pagina;
	private Integer quantidade;
	private TipoOrdenacao tipoOrdenacao = TipoOrdenacao.DESC;
	private String faixaValor;
	private Boolean compraDireta;
	private Integer anoProcesso;
	
	@Builder
	public ParametrosLicitacao(String geral, String situacao, List<String> modalidade, String dataInicio,
			String dataFim, Double valor, Integer ano, Integer pagina, Integer quantidade,
			TipoOrdenacao tipoOrdenacao, String faixaValor, Boolean compraDireta, Integer anoProcesso) {
		super();
		this.geral = geral;
		this.situacao = situacao;
		this.modalidade = modalidade;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.valor = valor;
		this.ano = ano;
		this.pagina = pagina == null ? 1 : pagina;
		this.quantidade = quantidade == null ? 10 : quantidade;
		this.tipoOrdenacao = tipoOrdenacao;
		this.faixaValor = faixaValor;
		this.compraDireta = compraDireta;
		this.anoProcesso = anoProcesso;
	}
}
