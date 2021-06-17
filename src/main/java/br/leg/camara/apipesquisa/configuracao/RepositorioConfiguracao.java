package br.leg.camara.apipesquisa.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.EnqueteRest;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.LicitacaoRest;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.aplicacao.api.enquete.RepositorioEnquete;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.RepositorioLicitacao;

@Configuration
public class RepositorioConfiguracao {

	@Value("${spring.elasticsearch.jest.uris}")
	private String[] esUrls;
	
	@Autowired 
	ResourceLoader resourceLoader;

	@Bean
	public PesquisaElasticSearchImpl pesquisaElasticSearch() {
		return new PesquisaElasticSearchImpl(esUrls, resourceLoader);
	}

	@Bean
	public RepositorioEnquete repositorioEnquete(PesquisaElasticSearchImpl pesquisaElasticSearch) {
		return new EnqueteRest(pesquisaElasticSearch);
	}
	
	
	
	@Bean
	public RepositorioLicitacao repositorioLicitacao(PesquisaElasticSearchImpl pesquisaElasticSearch) {
		return new LicitacaoRest(pesquisaElasticSearch);
	}
	
}
