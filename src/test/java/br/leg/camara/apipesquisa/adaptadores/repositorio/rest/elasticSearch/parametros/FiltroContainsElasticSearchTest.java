package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FiltroContainsElasticSearchTest {
	
	@Test
	public void parametroBasico() {
		FiltroContainsElasticSearch contem = new FiltroContainsElasticSearch();
		contem.and("modalidade", "Dispensa de Licitação");
		assertEquals("modalidade: \"Dispensa de Licitação\"", contem.getQuery());
	}
	
	@Test
	public void sintaxeDaQueryString() {
		FiltroContainsElasticSearch contem = new FiltroContainsElasticSearch();
		contem.and("modalidade", "Dispensa de Licitação");
		contem.and("situacao", "Jugada");
		String sintaxe = "{\"query_string\":{\"default_operator\":\"AND\",\"query\":\"situacao: \\\"Jugada\\\" AND modalidade: \\\"Dispensa de Licitação\\\"\"}}";
		assertEquals(sintaxe, contem.queryString(OperadorAndOr.AND).toString());
	}

	
	@Test
	public void passandoVariosTiposdDeParametros() {
		FiltroContainsElasticSearch contem = new FiltroContainsElasticSearch();
		contem.and("modalidade", "Dispensa de Licitação")
			.and("situacao", "Julgada")
			.and("numeroProcesso", Arrays.asList(115935, 115935, 115935))
			.and("anoProcesso", 2015);
		
		assertEquals("anoProcesso:2015 AND (numeroProcesso:\"115935\" OR numeroProcesso:\"115935\" OR numeroProcesso:\"115935\") AND situacao: \"Julgada\" AND modalidade: \"Dispensa de Licitação\"", contem.getQuery());
	}

}
