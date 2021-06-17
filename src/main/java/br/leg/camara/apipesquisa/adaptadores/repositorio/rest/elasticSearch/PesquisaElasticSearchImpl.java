package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.List;

import org.springframework.core.io.ResourceLoader;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PesquisaElasticSearchImpl {

	private final JestClient esClient;
	private final Template template;
	private static String ARQUIVO_GENERICO_JSON = "generico";
	
	public PesquisaElasticSearchImpl(String[] esUrls, ResourceLoader resourceLoader) {
		List<String> urls = new ArrayList<>();
		Collections.addAll(urls, esUrls);

		log.info(">>> Servidores de busca configurados: " + String.join(";", urls));
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(urls).multiThreaded(true).build());
		this.esClient = factory.getObject();
		this.template = new Template(resourceLoader);
	}
	
	public PesquisaElasticSearch pesquisar(ParametrosParaTemplateElasticSearch parametros, String nomeArquivo) {
		String jsonQueryES = template.configurar(parametros, nomeArquivo);
		return pesquisarNoElasticSearch(nomeArquivo, jsonQueryES);
	}
	
	/**
	 * Pesquisa em vários indices.
	 * O uso do arquivo json para pesquisa não é obrigatório, se não passado é atribuido um arquivo genérico, 
	 * podendo atribuir todos filtro que constam na class ParametrosParaTemplateElasticSearch
	 * @param parametros
	 * @param indicesParaPesquisa
	 * @param nomeArquivoJson
	 * @return
	 */
	public PesquisaElasticSearch pesquisar(ParametrosParaTemplateElasticSearch parametros, Collection<String> indicesParaPesquisa, String nomeArquivoJson) {
		String arquivo = nomeArquivoJson != null ? nomeArquivoJson : ARQUIVO_GENERICO_JSON;
		String jsonQueryES = template.configurar(parametros, arquivo);
		return pesquisarNoElasticSearch(indicesParaPesquisa, jsonQueryES);
	}

	public PesquisaElasticSearch pesquisar(ParametrosParaTemplateElasticSearch parametros, String contexto, String parametrosQueryPersonalizados) {
		String jsonQueryES = template.configurar(parametros, contexto, parametrosQueryPersonalizados);
		return pesquisarNoElasticSearch(contexto, jsonQueryES);
	}
	
	public PesquisaElasticSearch pesquisar(String elasticsearchFile, ParametrosParaTemplateElasticSearch parametros, String contexto) {
		String jsonQueryES = template.configurar(parametros, elasticsearchFile);
		return pesquisarNoElasticSearch(contexto, jsonQueryES);
	}

	private PesquisaElasticSearch pesquisarNoElasticSearch(String nomeIndice, String jsonQueryES) {
		
		Search search = new Search.Builder(jsonQueryES).addIndex(nomeIndice).build();
		try {
			return new PesquisaElasticSearch(this.esClient.execute(search));
		} catch (SocketTimeoutException e) {			
			throw new RuntimeException("Ocorreu timeout ao consultar o ES", e);
		} catch (Exception e) {			
			throw new RuntimeException("Ocorreu um erro ao pesquisar", e);
		}
	}

	private PesquisaElasticSearch pesquisarNoElasticSearch(Collection<String> indicesParaPesquisa, String jsonQueryES) {
		Search search = new Search.Builder(jsonQueryES).addIndices(indicesParaPesquisa).build();
		try {
			return new PesquisaElasticSearch(this.esClient.execute(search));
		} catch (SocketTimeoutException e) {			
			throw new RuntimeException("Ocorreu timeout ao consultar o ES", e);
		} catch (Exception e) {			
			throw new RuntimeException("Ocorreu um erro ao pesquisar ", e);
		}
	}
}
