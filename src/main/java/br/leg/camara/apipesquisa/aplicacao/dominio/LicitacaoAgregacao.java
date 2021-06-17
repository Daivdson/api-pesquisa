package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.List;

import lombok.Getter;

@Getterpublic class LicitacaoAgregacao {
	
	private List<ResultadoAgregacao> situacao;
	private List<ResultadoAgregacao> situacaoSimplificada;
	private List<ResultadoAgregacao> ano;
	private List<ResultadoAgregacao> modalidade; 
	private List<ResultadoAgregacao> faixaValor;
	private List<ResultadoAgregacao> anoProcesso;
	
	public LicitacaoAgregacao(List<ResultadoAgregacao> situacao, List<ResultadoAgregacao> situacaoSimplificada,List<ResultadoAgregacao> ano,
			List<ResultadoAgregacao> modalidade, List<ResultadoAgregacao> faixaValor, List<ResultadoAgregacao> anoProcesso) {
		super();
		this.situacao = situacao;
		this.situacaoSimplificada = situacaoSimplificada;
		this.ano = ano;
		this.modalidade = modalidade;
		this.faixaValor = faixaValor;
		this.anoProcesso = anoProcesso;
	}
	
	

}
