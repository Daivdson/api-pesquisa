package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.ParametrosCota;
import br.leg.camara.apipesquisa.aplicacao.dominio.Cota;
import br.leg.camara.apipesquisa.aplicacao.dominio.CotaValorDespesa;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

public class RepositorioCotasRestTest {
	
	@Rule
	public WireMockRule selegWireMockRule = new WireMockRule(options().dynamicPort());
	
	private ResourceLoader resourceLoader = new AnnotationConfigServletWebServerApplicationContext();
	private RepositorioCotasRest repositorio;

	@Before	
	public void criaRepositorio() {
		PesquisaElasticSearchImpl elasticSearch = new PesquisaElasticSearchImpl(urlMockElasticSearch(), resourceLoader);
		this.repositorio = new RepositorioCotasRest(elasticSearch);
	}
	
	@Test
	public void pesquisarDeveRetornarListaDeDeputados() {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.mesAnoFinal("08-2020")
				.mesAnoInicial("06-2020")
				.build();
		
		Map<String, Object> resultado = repositorio.cotaDe2Niveis(param, "tipoDespesa", "tipoDespesa.keyword", "deputado", "deputado.keyword", new Ordenacao(null, null));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> cotas = (List<Map<String, Object>>) resultado.get("resultados");
		@SuppressWarnings("unchecked")
		List<CotaValorDespesa> deputadosDoPrimeiroTipoDespesa = (List<CotaValorDespesa>) cotas.get(0).get("deputado");
				
				
		assertNotNull(resultado);
		
		assertEquals(5952942.880163908, resultado.get("total"));
		assertEquals("TELEFONIA", cotas.get(0).get("nome"));
		assertEquals("Áurea Carolina", deputadosDoPrimeiroTipoDespesa.get(0).getNome());
	}
	
	
	@Test
	public void pesquisarDeveRetornarListaMesAnoDeUmDeputado() {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa("PASSAGEM AÉREA - REEMBOLSO")
				.pesquisaPorDeputado(true)
				.deputado(Arrays.asList("Marcelo Ramos"))
				.build();
		
		Map<String, Object> resultado =  repositorio.cotaDe1Nivel(parametros, new Ordenacao(null, null), "mesAno", "mesAno.keyword");
		@SuppressWarnings("unchecked")
		List<CotaValorDespesa> despesaDeputado = (List<CotaValorDespesa>) resultado.get("resultados");

		assertNotNull(resultado);
		
		assertEquals("7-2020", despesaDeputado.get(0).getNome());
		assertTrue(despesaDeputado.get(0).getValor().equals(12022.819763183594));
	}
	
	@Test
	public void pesquisarDeveRetornarCotasDoMesAnoDeLideranca() {
		
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa("COMBUSTÍVEIS E LUBRIFICANTES.")
				.pesquisaPorLideranca(true)
				.mesAno("07-2020")
				.lideranca(Arrays.asList("LIDERANÇA DO PT"))
				.build(); 
		
		Resultado<Cota> resultado = repositorio.cotaDe0Nivel(parametros);
		List<Cota> cotas = resultado.getResultado();
		assertNotNull(resultado);
		
		assertEquals(2, resultado.getQuantidadeTotal().intValue());
		assertEquals("LIDERANÇA DO PT", cotas.get(0).getLideranca());
		assertTrue(cotas.get(0).getValorDespesa().equals(150.08));
		assertEquals(5952942.880163908 ,resultado.getAgregacao().get("totalDespesa"));
		
	}

	private String[] urlMockElasticSearch() {
		String url = "http://localhost:" + selegWireMockRule.port();
		return new String[]{url};
	}
}
