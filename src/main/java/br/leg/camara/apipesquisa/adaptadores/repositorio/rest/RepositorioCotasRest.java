package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores.ConversorAgregacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores.ConversorCotas;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.BucketsDTO;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ModoOrdenacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.OperadorAndOr;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.ParametrosCota;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.RepositorioCotas;
import br.leg.camara.apipesquisa.aplicacao.dominio.Cota;
import br.leg.camara.apipesquisa.aplicacao.dominio.CotaValorDespesa;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.RangeData;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;
import br.leg.camara.apipesquisa.aplicacao.dominio.ResultadoAgregacao;

public class RepositorioCotasRest implements RepositorioCotas {

	private static final String CAMPO_ORDENACAO = "data";
	private static final String NOME_INDICE = "cotas";
	private final ConversorAgregacao conversorAgg;
	private final ConversorCotas conversorCotas;
	private final PesquisaElasticSearchImpl pesquisaElasticSearch;
	private static String keyword = ".keyword";

	public RepositorioCotasRest(PesquisaElasticSearchImpl pesquisaElasticSearch) {
		this.pesquisaElasticSearch = pesquisaElasticSearch;
		this.conversorAgg = new ConversorAgregacao();
		this.conversorCotas = new ConversorCotas();
	}

	private ParametrosParaTemplateElasticSearch criarParametroES(ParametrosCota parametros) {
		ParametrosParaTemplateElasticSearch paramEs = ParametrosParaTemplateElasticSearch.builder()
				.pagina(parametros.getPagina()).quantidade(parametros.getQuantidade())
				.ordenacao(singletonList(
						Ordenacao.builder().campo(CAMPO_ORDENACAO).tipoOrdenacao(parametros.getOrdenacao()).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND).build();

		if (parametros.getGeral() != null) {
			paramEs.criarFiltroContains("", parametros.getGeral());
		}
		if (parametros.getSiglaPartido() != null) {
			paramEs.criarFiltro("siglaPartido", parametros.getSiglaPartido());
		}
		if (parametros.getUf() != null) {
			paramEs.criarFiltro("siglaUF", parametros.getUf().toString());
		}
		if (parametros.getTipoDespesa() != null) {
			paramEs.criarFiltro("tipoDespesa".concat(keyword), parametros.getTipoDespesa());
		}
		if (parametros.getMesAno() != null) {
			paramEs.criarFiltro("mesAno", parametros.getMesAno());
		}
		if (parametros.getCnpjCpfFornecedor() != null) {
			paramEs.criarFiltro("cnpjCpfFornecedor", parametros.getCnpjCpfFornecedor());
		}
		if (parametros.getNumeroDocumento() != null) {
			paramEs.criarFiltro("numeroDocumento", parametros.getNumeroDocumento());
		}
		if (parametros.getDeputado() != null && !parametros.getDeputado().isEmpty()) {
			paramEs.criarFiltroTerms("deputado", parametros.getDeputado());
		}
		if (parametros.getLideranca() != null && !parametros.getLideranca().isEmpty()) {
			paramEs.criarFiltroTerms("lideranca", parametros.getLideranca());
		}
		if (parametros.getTipoSolicitante() != null) {
			paramEs.criarFiltro("tipoSolicitante", parametros.getTipoSolicitante().getValue());
		}
		
		if (parametros.getNomeFornecedorCpfCnpj() != null && !parametros.getNomeFornecedorCpfCnpj().isEmpty()) {
			paramEs.criarFiltroTerms("nomeFornecedorCpfCnpj", parametros.getNomeFornecedorCpfCnpj());
		}
		
		if (parametros.getDeputadoPartidoUfId() != null && !parametros.getDeputadoPartidoUfId().isEmpty()) {
			paramEs.criarFiltroTerms("deputadoUfId", parametros.getDeputadoPartidoUfId());
		}
		
		if (parametros.getDeputado() != null && !parametros.getDeputado().isEmpty()) {
			paramEs.criarFiltroTerms("deputado", parametros.getDeputado());
		}
		
		if (parametros.getTipoDespesas() != null && !parametros.getTipoDespesas().isEmpty()) {
			paramEs.criarFiltroTerms("tipoDespesa".concat(keyword), parametros.getTipoDespesas());
		}
		
		// ver forma de pegar o priodo
		if (parametros.getMesAnoFinal() != null && parametros.getMesAnoInicial() != null) {
			RangeData range = new RangeData(parametros.getDataInicio(), parametros.getDataFim(), "dataCompetencia");
			paramEs.criarFiltro().addRangeData(range);
		}

		return paramEs;
	}

	@Override
	public Resultado<Cota> listaGeral(ParametrosCota parametros) {
		ParametrosParaTemplateElasticSearch paramEs = criarParametroES(parametros);

		paramEs.criarAgregacao("deputado", "nomeParlamentar"+keyword, 100, TipoOrdenacao.DESC, ModoOrdenacao.KEY);
		paramEs.criarAgregacao("liderancas", "lideranca"+keyword, 100, TipoOrdenacao.DESC, ModoOrdenacao.KEY);
		paramEs.criarAgregacao("tipoDespesas", "tipoDespesa"+keyword, 100, TipoOrdenacao.DESC, ModoOrdenacao.KEY);

		PesquisaElasticSearch resultadoPesquisa = pesquisaElasticSearch.pesquisar(paramEs, NOME_INDICE);

		Long quantidadeTotal = resultadoPesquisa.getQuantidadeTotalDeRegistros();
		List<Cota> listaCotas = resultadoPesquisa.buscarConteudos(Cota.class);

		List<BucketsDTO> deputado = resultadoPesquisa.buscarBucketAgregacoes("deputado");
		List<BucketsDTO> lideranca = resultadoPesquisa.buscarBucketAgregacoes("liderancas");
		List<BucketsDTO> tipoDespesas = resultadoPesquisa.buscarBucketAgregacoes("tipoDespesas");

		Map<String, Object> agregacao = new HashMap<String, Object>();
		agregacao.put("deputado", conversorAgg.converter(deputado));
		agregacao.put("lideranca", conversorAgg.converter(lideranca));
		agregacao.put("tipoDespesas", conversorAgg.converter(tipoDespesas));

		return new Resultado<Cota>(quantidadeTotal, listaCotas, agregacao);
	}
	
	@Override
	public Map<String, Object> agregacao(ParametrosCota parametros, String rotuloCampo, String campo) {
		parametros.setQuantidade(0);
		ParametrosParaTemplateElasticSearch paramEs = criarParametroES(parametros);
		
		paramEs.criarAgregacao(codigoAgregacaoDe1Nivel(rotuloCampo, campo, 
				new Ordenacao("_key", TipoOrdenacao.ASC), 
				parametros.getQuantidadeNivel1()));
		
		PesquisaElasticSearch resultadoPesquisa = pesquisaElasticSearch.pesquisar(paramEs, NOME_INDICE);
		
		JsonObject  jsonQuantidadeTotal = resultadoPesquisa.buscarAgregacaoGeral("quantidadeTotal");
		List<BucketsDTO> bucket = resultadoPesquisa.buscarBucketAgregacoes(rotuloCampo);
		
		if(bucket == null || jsonQuantidadeTotal == null) {
			return new HashMap<String, Object>();
		}
		
		Integer quantidadeTotal = jsonQuantidadeTotal.getAsJsonObject().get("value").getAsInt();
		List<ResultadoAgregacao> bucketsDaAgregacao = conversorAgg.converter(bucket);
		
		Map<String, Object> agregacao = new HashMap<String, Object>();
		agregacao.put(rotuloCampo, bucketsDaAgregacao);
		agregacao.put("quantidade", quantidadeTotal);
		return agregacao;
	}
	
	@Override
	public Map<String, Object> cotaDe2Niveis(
			ParametrosCota parametros,
			String rotuloPrimeiroNivel,
			String campoPrimeiroNivel,
			String rotuloSegundoNivel,
			String campoSegundoNivel,
			Ordenacao ordenacao) {
		
		parametros.setQuantidade(0);
		ParametrosParaTemplateElasticSearch paramEs = criarParametroES(parametros);
		
		String codigoAgregacao = codigoAgregacaoDe2NiveisComSoma(
				rotuloPrimeiroNivel,
				campoPrimeiroNivel,
				rotuloSegundoNivel,
				campoSegundoNivel, 
				ordenacao, 
				parametros.getQuantidadeNivel1(), 
				parametros.getQuantidadeNivel2());
		
		paramEs.criarAgregacao(codigoAgregacao);
		
		PesquisaElasticSearch resultadoPesquisa = pesquisaElasticSearch.pesquisar(paramEs, NOME_INDICE);
		
		JsonObject  jsonValorDespesa = resultadoPesquisa.buscarAgregacaoGeral("valorTotal");
		JsonObject  jsonQuantidadeTotal = resultadoPesquisa.buscarAgregacaoGeral("quantidadeTotal");
		JsonObject  jsonAgregacao = resultadoPesquisa.buscarAgregacaoGeral(rotuloPrimeiroNivel);
		
		if(jsonAgregacao == null) {
			return new HashMap<String, Object>();
		}
		
		Double somatorioTotalDeDespesa = jsonValorDespesa.getAsJsonObject().get("value").getAsDouble();
		Integer quantidadeTotal = jsonQuantidadeTotal.getAsJsonObject().get("value").getAsInt();
		
		JsonArray buckets = jsonAgregacao.getAsJsonArray("buckets");
		
		List<Map<String, Object>> agregagacaoDespesa = conversorCotas.converterBuketsDe2Niveis(buckets, rotuloSegundoNivel);
		
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("total", somatorioTotalDeDespesa);
		resultado.put("quantidadeTotal", quantidadeTotal);
		resultado.put("resultados", agregagacaoDespesa);
	
		return resultado;
	}
	
	@Override
	public Map<String, Object> cotaDe1Nivel(ParametrosCota parametros, Ordenacao ordenacao, String rotuloPrimeiroNivel, String campoPrimeiroNivel) {
		parametros.setQuantidade(0);
		ParametrosParaTemplateElasticSearch paramEs = criarParametroES(parametros);
		
		if(parametros.getTipoSolicitante() == null) {
			return new HashMap<String, Object>();
		}
		
		String codigoAgregacao = codigoAgregacaoDe1NivelComSoma(rotuloPrimeiroNivel, campoPrimeiroNivel, ordenacao, parametros.getQuantidadeNivel1());
		
		paramEs.criarAgregacao(codigoAgregacao);
		
		PesquisaElasticSearch resultadoPesquisa = pesquisaElasticSearch.pesquisar(paramEs, NOME_INDICE);
		
		JsonObject  jsonValorDespesa = resultadoPesquisa.buscarAgregacaoGeral("valorTotal");
		JsonObject  jsonQuantidadeTotal = resultadoPesquisa.buscarAgregacaoGeral("quantidadeTotal");
		JsonObject  jsonAgregacao = resultadoPesquisa.buscarAgregacaoGeral(rotuloPrimeiroNivel);
		
		if(jsonAgregacao == null || jsonValorDespesa == null) {
			return new HashMap<String, Object>();
		}
		
		Double somatorioTotalDeDespesa = jsonValorDespesa.getAsJsonObject().get("value").getAsDouble();
		Integer quantidadeTotal = jsonQuantidadeTotal.getAsJsonObject().get("value").getAsInt();
		JsonArray bucketsDaAgregacao = jsonAgregacao.getAsJsonArray("buckets");
		
		List<CotaValorDespesa> despesaSolicitante = conversorCotas.converterBuketsDe1Nivel(bucketsDaAgregacao);
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("total", somatorioTotalDeDespesa);
		resultado.put("quantidadeTotal", quantidadeTotal);
		resultado.put("resultados", despesaSolicitante);
	
		return resultado;
	}
	
	
	@Override
	public Resultado<Cota> cotaDe0Nivel(ParametrosCota parametros) {
		ParametrosParaTemplateElasticSearch paramEs = criarParametroES(parametros);
		paramEs.criarAgregacao(codigoAgregacaoDeSomaValorDespesa());
		
		PesquisaElasticSearch resultadoPesquisa = pesquisaElasticSearch.pesquisar(paramEs, NOME_INDICE);
		List<Cota> listaCotas = resultadoPesquisa.buscarConteudos(Cota.class);
		Long totalDeRegistro = resultadoPesquisa.getQuantidadeTotalDeRegistros();
		
		JsonObject  jsonValorDespesa = resultadoPesquisa.buscarAgregacaoGeral("valorTotal");
		
		if(jsonValorDespesa == null) {
			return new Resultado<Cota>(totalDeRegistro, listaCotas);
		}

		Double totalDeDespesa = jsonValorDespesa.getAsJsonObject().get("value").getAsDouble();
		
		Map<String, Object> agregacao = new HashMap<String, Object>();
		agregacao.put("totalDespesa", totalDeDespesa);

		return new Resultado<Cota>(totalDeRegistro, listaCotas, agregacao);
	}
	
	
	private String codigoAgregacaoDe2NiveisComSoma (
			String rotuloPrimeiroNivel,
			String campoPrimeiroNivel,
			String rotuloSegundoNivel, 
			String campoSegundoNivel,
			Ordenacao ordenacao, 
			Integer quantidadeNivel1, 
			Integer quantidadeNivel2) {
		return codigoAgregacaoDeSomaValorDespesa().concat(",")
				.concat(codigoAgregacaoDeQuantidadeTotalBuckets(campoPrimeiroNivel))
				.concat(",")
				.concat(
				"\""+rotuloPrimeiroNivel+"\": {\n" + 
				"        	\"terms\": {\n" + 
				"            	\"field\": \""+campoPrimeiroNivel+"\",\n" + 
				"				\"size\": "+quantidadeNivel1+",\n" + 
				"				\"order\" : { \""+ordenacao.getCampo()+"\" : \""+ordenacao.getTipoOrdenacao()+"\" }\n" + 
				"         	},\n" + 
				"			\"aggs\": {\n" + 
				"				\"valor\" : {\n" + 
				"					\"sum\": { \"field\": \"valorDespesa\"}\n" + 
				"					\n" + 
				"				},\n" + 
				"				\""+rotuloSegundoNivel+"\" : {\n" + 
				"					\"terms\": {\n" + 
				"						\"field\": \""+campoSegundoNivel+"\",\n" + 
				"						\"size\": "+quantidadeNivel2+",\n" +
				"						\"order\" : { \""+ordenacao.getCampo()+"\" : \""+ordenacao.getTipoOrdenacao()+"\" }\n" + 
				"					},\n" + 
				"					\"aggs\": {\n" + 
				"						\"valor\" : {\n" + 
				"							\"sum\": { \"field\": \"valorDespesa\" }\n" + 
				"						}\n" + 
				"					}\n" + 
				"				}\n" + 
				"			}\n" + 
				"    	}");
	}
	
	private String codigoAgregacaoDe1NivelComSoma(
			String rotuloPrimeiroNivel, 
			String campoPrimeiroNivel,
			Ordenacao ordenacao, 
			Integer quantidade) {
		return codigoAgregacaoDeSomaValorDespesa()
				.concat(",")
				.concat(codigoAgregacaoDeQuantidadeTotalBuckets(campoPrimeiroNivel))
				.concat(",")
				.concat(
				"\""+rotuloPrimeiroNivel+"\": {\n" + 
				"        	\"terms\": {\n" + 
				"            	\"field\": \""+campoPrimeiroNivel+"\",\n" + 
				"				\"size\": "+quantidade+",\n" +
				"				\"order\" : { \""+ordenacao.getCampo()+"\" : \""+ordenacao.getTipoOrdenacao()+"\" }\n" + 
				"         	},\n" + 
				"			\"aggs\": {\n" + 
				"				\"valor\" : {\n" + 
				"					\"sum\": { \"field\": \"valorDespesa\"}\n" + 
				"				}\n" + 
				"			}\n" + 
				"    	}");
	}
	
	private String codigoAgregacaoDe1Nivel(
			String rotuloPrimeiroNivel, 
			String campoPrimeiroNivel,
			Ordenacao ordenacao, 
			Integer quantidade) {
		return codigoAgregacaoDeQuantidadeTotalBuckets(campoPrimeiroNivel)
				.concat(",")
				.concat(
				"\""+rotuloPrimeiroNivel+"\": {\n" + 
				"        	\"terms\": {\n" + 
				"            	\"field\": \""+campoPrimeiroNivel+"\",\n" + 
				"				\"size\": "+quantidade+",\n" +
				"				\"order\" : { \""+ordenacao.getCampo()+"\" : \""+ordenacao.getTipoOrdenacao()+"\" }\n" + 
				"         	}\n" + 
				"    	}");
	}
	
	private String codigoAgregacaoDeSomaValorDespesa() {
		return "\"valorTotal\" : {\n" + 
				"	\"sum\": { \"field\": \"valorDespesa\"}	\n" + 
				"}";
	}
	
	private String codigoAgregacaoDeQuantidadeTotalBuckets(String campo) {
		return "\"quantidadeTotal\" : {\n" + 
				"	\"cardinality\": { \"field\": \""+campo+"\"}	\n" + 
				"}";
	}

}