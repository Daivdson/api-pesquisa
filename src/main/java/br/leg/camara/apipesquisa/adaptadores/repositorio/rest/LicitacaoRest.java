package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import static java.util.Collections.singletonList;

import java.util.List;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores.ConversorAgregacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.BucketsDTO;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ModoOrdenacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util.DataUtil;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util.Util;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.ParametrosLicitacao;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.RepositorioLicitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Licitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.LicitacaoAgregacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.RangeData;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

public class LicitacaoRest implements RepositorioLicitacao{

	private static String contexto = "licitacoes";

	private final ConversorAgregacao conversorAgg;
	private final PesquisaElasticSearchImpl repositorio;
	private static final String CAMPO_DATA = "dataPublicacao";

	public LicitacaoRest(PesquisaElasticSearchImpl pesquisaElasticSearch) {
		this.repositorio = pesquisaElasticSearch;
		this.conversorAgg = new ConversorAgregacao();
	}
	
	@Override
	public Resultado<Licitacao> listaGeral(ParametrosLicitacao parametros) {
		ParametrosParaTemplateElasticSearch parametrosES = criarParametroPadraoES(parametros);
		PesquisaElasticSearch pesquisa = repositorio.pesquisar(parametrosES,contexto);
		
		List<Licitacao> buscarConteudos = pesquisa.buscarConteudos(Licitacao.class);
		return new Resultado<>(pesquisa.getQuantidadeTotalDeRegistros(), buscarConteudos);
	}
	
	private ParametrosParaTemplateElasticSearch criarParametroPadraoES(ParametrosLicitacao parametros) {
		Util util = new Util();
		ParametrosParaTemplateElasticSearch es = ParametrosParaTemplateElasticSearch.builder()
				.pagina(parametros.getPagina())
				.quantidade(parametros.getQuantidade())
				.ordenacao(singletonList(Ordenacao.builder()
							.campo(CAMPO_DATA)
							.tipoOrdenacao(parametros.getTipoOrdenacao())
							.build())
						)
				.build();
		
		if(parametros.getGeral() != null) {
			String campoCaracterEspecialEscapado = parametros.getGeral().replaceAll("[^'' \\w]", "\\\\$0");
			es.criarFiltroContains("", campoCaracterEspecialEscapado.toString())
				.and("valor", parametros.getValor())
				.and("modalidade", parametros.getModalidade());
		} else {
			es.criarFiltroContains("", parametros.getGeral())
			.and("valor", parametros.getValor())
			.and("modalidade", parametros.getModalidade());
		}

		
		if(parametros.getCompraDireta() != null) {
			es.criarFiltroContains("compraDireta", parametros.getCompraDireta());
		}
		
		es.criarFiltro("ano", parametros.getAno())
			.add("situacaoSimplificada", parametros.getSituacao())
			.add("faixaValor", util.tratarParametroFaixaValor(parametros.getFaixaValor()))
			.add("anoProcesso", parametros.getAnoProcesso());
		
		if(parametros.getDataInicio() != null && parametros.getDataFim() != null) {
			RangeData range = new RangeData(DataUtil.conversorData(parametros.getDataInicio()), 
					DataUtil.conversorData(parametros.getDataFim()), 
					CAMPO_DATA);
			es.criarFiltro().addRangeData(range);
		}
		return es;
	}
	
	@Override
	public LicitacaoAgregacao agregacao(ParametrosLicitacao parametros) {
		ParametrosParaTemplateElasticSearch parametrosES = criarParametroPadraoES(parametros);
		parametrosES.criarAgregacao("situacao", "situacao.keyword", 100, TipoOrdenacao.ASC, ModoOrdenacao.KEY);
		parametrosES.criarAgregacao("situacaosimplificada", "situacaoSimplificada.keyword", 100, TipoOrdenacao.ASC, ModoOrdenacao.KEY);
		parametrosES.criarAgregacao("ano", "ano", 100, TipoOrdenacao.DESC, ModoOrdenacao.KEY);
		parametrosES.criarAgregacao("modalidade", "modalidade.keyword", 100, TipoOrdenacao.ASC, ModoOrdenacao.KEY);
		parametrosES.criarAgregacao("faixaValor", "faixaValor.keyword", 100, TipoOrdenacao.ASC, ModoOrdenacao.COUNT);
		parametrosES.criarAgregacao("anoProcesso", "anoProcesso", 100, TipoOrdenacao.ASC, ModoOrdenacao.COUNT);
		
		PesquisaElasticSearch pesquisa = repositorio.pesquisar(parametrosES, contexto);
		
		List<BucketsDTO> situacao = pesquisa.buscarBucketAgregacoes("situacao");
		List<BucketsDTO> situacaoSimplificada = pesquisa.buscarBucketAgregacoes("situacaosimplificada");
		List<BucketsDTO> ano = pesquisa.buscarBucketAgregacoes("ano");
		List<BucketsDTO> modalidade = pesquisa.buscarBucketAgregacoes("modalidade");
		List<BucketsDTO> faixaValor = pesquisa.buscarBucketAgregacoes("faixaValor");
		List<BucketsDTO> anoProcesso = pesquisa.buscarBucketAgregacoes("anoProcesso");
		
		return new LicitacaoAgregacao(
				conversorAgg.converter(situacao),
				conversorAgg.converter(situacaoSimplificada),
				conversorAgg.converter(ano),
				conversorAgg.converter(modalidade),
				conversorAgg.converter(faixaValor),
				conversorAgg.converter(anoProcesso));
	}

	@Override
	public Licitacao licitacao(int id) {
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder().build();

		parametrosES.criarFiltro("id", id);

		PesquisaElasticSearch pesquisa = repositorio.pesquisar(parametrosES, contexto);
		List<Licitacao> buscarConteudos = pesquisa.buscarConteudos(Licitacao.class);

		return buscarConteudos.get(0);
	}

}
