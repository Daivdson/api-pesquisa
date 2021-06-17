package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.BucketsDTO;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.ChildrenAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PesquisaElasticSearch {

	private SearchResult searchResult;
	
	public PesquisaElasticSearch(SearchResult searchResult) {
		this.searchResult = searchResult;
	}

	/**
	 * Retorna conteudo principal da pesquisa. Ex: hitis->hits->source(conteudo)
	 * @param type
	 * @return
	 */
	public <T> List<T> buscarConteudos(Class<T> type) {
		verificaResultadoBusca();
		return searchResult.getHits(type)
				.stream()
				.map(x ->
					x.source
				)
				.collect(Collectors.toList());
	}
	
	private void verificaResultadoBusca() {
		int status = searchResult.getResponseCode();if (status != 200) {
			log.warn("Status da requisição elasticsearch "+searchResult.getResponseCode());
			log.error("Resposta elasticSearch "+searchResult.getErrorMessage());
			throw new RuntimeException("Resultado da pesquisa no elasticsearch retornou erro.");
		}
	}
	
	/**
	 * Retorna bucket agregação pelo nome 
	 * É retornado na estrutura padrão do elasticSearch
	 * @param nomeAgregacao
	 * @return
	 */
	public List<BucketsDTO> buscarBucketAgregacoes(String nomeAgregacao) {
		verificaResultadoBusca();
		List<Entry> buckets = searchResult.getAggregations().getTermsAggregation(nomeAgregacao).getBuckets();
		return buckets.stream().map(b -> new BucketsDTO(b.getKey(), b.getCount())).collect(Collectors.toList());
	}
	
	/**
	 * Retorna Json de toda agregação
	 * @param nomeAgregacao
	 * @return
	 */
	public JsonObject buscarAgregacaoGeral(String nomeAgregacao) {
		verificaResultadoBusca();
		try {
			if(nomeAgregacao == null) {
				return searchResult.getJsonObject().getAsJsonObject("aggregations");
			}
			return searchResult.getJsonObject().getAsJsonObject("aggregations").getAsJsonObject(nomeAgregacao);
		} catch (NullPointerException e) {
			log.error("Não foi possível extrair os aggregations do resultado da consulta. Verifique sua consulta e veja se aggregations possui em sua respota.");
			return null;
		}
	}
	
	public ChildrenAggregation buscarAgregacaoGuardaChuva(String nomeAgregacao) {
		verificaResultadoBusca();
		return searchResult.getAggregations().getChildrenAggregation(nomeAgregacao);
	}
		
	public List<Entry> buscarAgregacaoEntry(String nomeAgregacao) {
		verificaResultadoBusca();
		return searchResult.getAggregations().getTermsAggregation(nomeAgregacao).getBuckets();
	}
	
	public Long getQuantidadeTotalDeRegistros() {
		verificaResultadoBusca();
		return searchResult.getTotal();
	}
	
}
