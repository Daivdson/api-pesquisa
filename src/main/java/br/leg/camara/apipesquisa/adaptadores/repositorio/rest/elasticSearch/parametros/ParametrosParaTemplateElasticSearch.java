package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ParametrosParaTemplateElasticSearch {
	private Integer pagina;
	private Integer quantidade;	
	private List<Ordenacao> ordenacao;
	private String[] camposASeremExcluidos;
	private List<String> campoQueDevemExistir;
	private OperadorAndOr tipoPesquisaQuery;
	private AgregacaoBucketElasticSearch agregacao = new AgregacaoBucketElasticSearch();
	
	private FiltroExcludenteElasticSearch filtroExcludente = new FiltroExcludenteElasticSearch();	
	private FiltroElasticSearch filtro = new FiltroElasticSearch();
	private FiltroContainsElasticSearch filtroQuery = new FiltroContainsElasticSearch();
	private FiltroTermsElasticSearch filtroTerms = new FiltroTermsElasticSearch();
	
	@Builder
	public ParametrosParaTemplateElasticSearch(Integer pagina, Integer quantidade, List<Ordenacao> ordenacao,
			String[] camposASeremExcluidos, List<String> campoQueDevemExistir, OperadorAndOr tipoPesquisaQuery) {
		this.pagina = pagina != null ? pagina : 0;
		this.quantidade = quantidade != null ? quantidade : 20;
		this.ordenacao = ordenacao;
		this.camposASeremExcluidos = camposASeremExcluidos;
		this.campoQueDevemExistir = campoQueDevemExistir != null ? campoQueDevemExistir : new ArrayList<>();
		this.tipoPesquisaQuery = tipoPesquisaQuery != null ? tipoPesquisaQuery : OperadorAndOr.AND;
	}

	private Integer getQuantidade() {
		return this.quantidade != null ? this.quantidade : 20;
	}
	
	private Integer getPagina() {
		return this.pagina != null && this.pagina != 0 ? (this.pagina-1): 0;
	}
	
	/**
	 * Parametro utilizados na query do ElasticSearch para comparar algum campo no query_string
	 */
	public FiltroContainsElasticSearch criarFiltroContains(String campo, String conteudo) {
		filtroQuery.and(campo, conteudo);
		return filtroQuery;
	}
	
	public FiltroContainsElasticSearch criarFiltroContains(String campo, boolean conteudo) {
		filtroQuery.and(campo, conteudo);
		return filtroQuery;
	}
	
	public FiltroElasticSearch criarFiltroManual(String codigo) {
		filtro.add(codigo);
		return filtro;
	}
	
	/**
	 * Filtro utilizado para filtrar dados da consulta
	 */
	public FiltroElasticSearch criarFiltro(String campo, String conteudo) {
		filtro.add(campo, conteudo);
		return filtro;
	}
	
	/**
	 * Filtro utilizado para filtrar dados da consulta
	 */
	public FiltroElasticSearch criarFiltro(String campo, Boolean conteudo) {
		filtro.add(campo, conteudo);
		return filtro;
	}
		
	/**
	 * Filtro utilizado para filtrar dados da consulta
	 */
	public FiltroElasticSearch criarFiltro(String campo, Integer conteudo) {
		filtro.add(campo, conteudo);
		return filtro;
	}
	
	public FiltroElasticSearch criarFiltro() {
		return filtro;
	}

	
	public FiltroTermsElasticSearch criarFiltroTerms(String campo, List<String> conteudo) {
		filtroTerms.adicionarTerms(campo, conteudo);
		return filtroTerms;
		
	}
	
	/**
	 * Filtro utilizado para montar Agregações de dados da consulta
	 */
	public AgregacaoBucketElasticSearch criarAgregacao(String nome, String campo, int quatidadeBucket, TipoOrdenacao ordem, ModoOrdenacao modo) {
		if (quatidadeBucket <= 0) {
			throw new RuntimeException("Quantidade numa agregação não pode ser 0 ou menor");
		}
		agregacao.add(nome, campo, quatidadeBucket, ordem, modo);
		return agregacao;
	}

	public AgregacaoBucketElasticSearch criarAgregacao(String nome, String campo, int quatidadeBucket, TipoOrdenacao ordem) {
		return criarAgregacao(nome, campo, quatidadeBucket, ordem, ModoOrdenacao.COUNT);
	}
	
	/**
	 * Filtro utilizado para montar Agregações de dados da consulta com expressão
	 */
	public AgregacaoBucketElasticSearch criarAgregacao(String nome, String campo, String expressao) {
		if (this.quantidade <= 0) {
			throw new RuntimeException("Quantidade numa agregação não pode ser 0 ou menor");
		}
		agregacao.add(nome, campo, expressao);
		return agregacao;
	}
	/**
	 * Filtro para agregação, podendo passar uma trecho em codigo de como deve ser a agregação.
	 */
	public AgregacaoBucketElasticSearch criarAgregacao(String codigoAgregacao) {
		agregacao.add(codigoAgregacao);
		return agregacao;
	}
	
	/**
	 * From indica a quantidade de documentos iniciais a serem ignorados
	 */
	public Integer getFrom() {
		return getSize() * getPagina();
	}
	
	/**
	 * Quantidade de registro por pagina
	 */
	public Integer getSize() {
		return getQuantidade();
	}
	
	/**
	 * Tipo e campo para ordena ressultado
	 */
	public String getSort() {
		String retorno = "{}";
		if (this.ordenacao != null && this.ordenacao.size() != 0) {
			retorno = "[" + this.ordenacao.stream().map(x -> "{\"" + x.getCampo() + "\":{\"order\":\"" + x.getTipoOrdenacao() + "\"" + sortNested(x) + "}}").collect(Collectors.joining(",")) + "]";
		}
		return retorno;
	}
	
	private String sortNested(Ordenacao ordenacao) {
		if (ordenacao.getCampoNested() != null) {
			return ", \"nested_path\" : \"" + ordenacao.getCampoNested() + "\"";
		}
		return "";
	}
	
	/**
	 * Implementado a opção excludes para remover campos indesejados
	 */
	public String getSource() throws JSONException {
		return excludes(this.camposASeremExcluidos).toString();
	}
	
	/**
	 * Lista de campos para serem excluidos
	 */
	private JSONObject excludes(String[] campos) throws JSONException {
		// Lista de campos a serem excluidos do resulta da pesquisa
		return new JSONObject().put("excludes", campos);
	}
	
	/**
	 * Adiciona filtro para resultado de pesquisa
	 */
	public List<JSONObject> getFilter() {
		return this.filtro.getFiltro();
	}

	public String getAgregacao() {
		return this.agregacao.toString();
	}
	
	/**
	 * Adicionar must da query, possuindo campos obrigatório e filtro pelos campos
	 */
	public List<JSONObject> getMust() {
		try {
			List<JSONObject> param = new ArrayList<>();
			param.add(filtroQuery.queryString(tipoPesquisaQuery));
			param.addAll(exists(this.campoQueDevemExistir));
			param.addAll(filtroTerms.getTerms());
			return param;
		} catch (JSONException e) {
			log.error("Erro ao montar o must", e);
		}
		return null;
	}
	
	/**
	 * Lista de campos que devem existir no resultado
	 */
	private List<JSONObject> exists(List<String> campoQueDevemExistir) throws JSONException {
		List<JSONObject> filter = new ArrayList<>();
		campoQueDevemExistir.forEach(valor -> {
			try {
				filter.add(new JSONObject().put("exists", new JSONObject().put("field", valor)));
			} catch (JSONException e) {
				throw new RuntimeException("Ocorreu um erro ao montar filtros para temaplate: ", e);
			}
		});
		return filter;
	}

	public FiltroExcludenteElasticSearch criarFiltroExcludente(String campo, String conteudo) {
		filtroExcludente.add(campo, conteudo);
		return filtroExcludente;
	}
	
	public List<JSONObject> getMustNot() {
		List<JSONObject> filter = new ArrayList<>();
		this.filtroExcludente.getFiltro().forEach(valor -> {
			try {
				filter.add(new JSONObject().put("match_phrase", valor));
			} catch (JSONException e) {
				throw new RuntimeException("Ocorreu um erro ao montar filtros para temaplate: ", e);
			}
		});
		return filter;
	}

}
