package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import static java.util.Collections.singletonList;

import java.util.List;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores.ConversorEnquete;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.EnqueteDTO;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.FiltroContainsElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.api.enquete.RepositorioEnquete;
import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;

public class EnqueteRest implements RepositorioEnquete{
	
	private static final String NUMERO_VOTACAO_EM_7_DIAS = "numeroVotacaoEm7Dias";
	private static final String NUMERO_VOTACAO_EM_1_MES = "numeroVotacaoEm1Mes";
	private static final String NUMERO_VOTACAO_EM_6_MESES = "numeroVotacaoEm6Meses";
	private static final String NUMERO_COMENTARIO_EM_7_DIAS = "numeroComentarioEm7Dias";
	private static final String NUMERO_COMENTARIO_EM_1_MES = "numeroComentarioEm1Mes";
	private static final String NUMERO_COMENTARIO_EM_6_MESES = "numeroComentarioEm6Meses";
	private static final String CONTEXTO = "enquetes";

	private final ConversorEnquete conversor;
	private final PesquisaElasticSearchImpl repositorio;


	public EnqueteRest(PesquisaElasticSearchImpl pesquisaElasticSearch) {
		this.repositorio = pesquisaElasticSearch;
		this.conversor = new ConversorEnquete();
	}
	
	@Override
	public List<Enquete> maisVotadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_VOTACAO_EM_7_DIAS, emTramitacao);
	}

	@Override
	public List<Enquete> maisVotadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_VOTACAO_EM_1_MES, emTramitacao);
	}

	@Override
	public List<Enquete> maisVotadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_VOTACAO_EM_6_MESES, emTramitacao);
	}

	@Override
	public List<Enquete> maisComentadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_COMENTARIO_EM_7_DIAS, emTramitacao);
	}

	@Override
	public List<Enquete> maisComentadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_COMENTARIO_EM_1_MES, emTramitacao);
	}

	@Override
	public List<Enquete> maisComentadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return realizaConsultaERetornaLista(quantidade, temaPortal, validoPortal, NUMERO_COMENTARIO_EM_6_MESES, emTramitacao);
	}
	
	private List<Enquete> realizaConsultaERetornaLista(Integer quantidade, String temaPortal, boolean validoPortal, String order, boolean emTramitacao) {
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.quantidade(quantidade)
				.ordenacao(singletonList(Ordenacao.builder().campo(order).tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.build();
		FiltroContainsElasticSearch filtroContains = parametrosES.criarFiltroContains("proposicao.temaPortal", temaPortal);
		
		if (validoPortal) {
			filtroContains.and("tituloEnquete:*");
			filtroContains.and("descricaoEnquete:*");
			filtroContains.and("url:*");
		}
		
		if (emTramitacao) {
			parametrosES.criarFiltro("proposicao.emTramitacao", "Sim");			
		}
		
		PesquisaElasticSearch pesquisa = repositorio.pesquisar(parametrosES, CONTEXTO);
		List<EnqueteDTO> buscarConteudos = pesquisa.buscarConteudos(EnqueteDTO.class);
		return conversor.convert(buscarConteudos);
	}

}
