package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.BucketsDTO;
import br.leg.camara.apipesquisa.aplicacao.dominio.ResultadoAgregacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.ResultadoAgregacaoApenasNomes;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

public class ConversorAgregacao {

	public List<ResultadoAgregacao> converter(List<BucketsDTO> source){
		return source.stream().map(this::converter).collect(Collectors.toList());
	}
	
	public List<ResultadoAgregacaoApenasNomes> converterApenasNomes(List<BucketsDTO> source){
		return source.stream().map(this::converterApenasNomes).collect(Collectors.toList());
	}

	private ResultadoAgregacao converter(BucketsDTO dto) {
		return ResultadoAgregacao.builder()
				.nome(trim(dto.getResultado()))
				.quantidade(dto.getQuantidade())
				.build();
	}
	
	private ResultadoAgregacaoApenasNomes converterApenasNomes(BucketsDTO dto) {
		return ResultadoAgregacaoApenasNomes.builder()
				.nome(trim(dto.getResultado()))
				.build();
	}
	
	public Long retornaAQuantidadeTotal(List<BucketsDTO> source) {
		if (!source.isEmpty()) {
			return source.stream().map(BucketsDTO::getQuantidade).reduce(Long::sum).get();
		} else {
			return 0L;
		}
	}

	public ResultadoAgregacao converter(Entry dto) {
		return new ResultadoAgregacao(dto.getKey(), dto.getCount());
	}
}
