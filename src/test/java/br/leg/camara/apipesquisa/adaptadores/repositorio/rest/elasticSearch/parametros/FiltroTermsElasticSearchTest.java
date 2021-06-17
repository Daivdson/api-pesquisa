package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FiltroTermsElasticSearchTest {
	
	
	@Test
	public void termVazio() {
		FiltroTermsElasticSearch  filtro = new FiltroTermsElasticSearch();
		
		assertEquals(new ArrayList<String>(), filtro.getTerms());
	}
	
	@Test
	public void term2valores() {
		FiltroTermsElasticSearch  filtro = new FiltroTermsElasticSearch();
		
		filtro.adicionarTerms("situacao", Arrays.asList("Juagada", "Em andamento"));
		
		assertEquals("[{\"terms\":{\"situacao\":[\"Juagada\",\"Em andamento\"]}}]", filtro.getTerms().toString());
	}
	
	@Test
	public void termVariosvalores() {
		FiltroTermsElasticSearch  filtro = new FiltroTermsElasticSearch();
		
		filtro.adicionarTerms("situacao", Arrays.asList("Juagada", "Em andamento"));
		filtro.adicionarTerms("referencia", Arrays.asList("primeiro", "segundo"));
		filtro.adicionarTerms("status", Arrays.asList("aprovado", "pendente"));
		String terms = "[{\"terms\":{\"situacao\":[\"Juagada\",\"Em andamento\"]}}, {\"terms\":{\"referencia\":[\"primeiro\",\"segundo\"]}}, {\"terms\":{\"status\":[\"aprovado\",\"pendente\"]}}]";
		assertEquals(terms, filtro.getTerms().toString());
	}
	
	

}
