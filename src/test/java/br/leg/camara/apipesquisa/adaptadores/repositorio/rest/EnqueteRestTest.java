package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores.ConversorEnquete;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.EnqueteDTO;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class EnqueteRestTest {
	
	@Rule
	public WireMockRule selegWireMockRule = new WireMockRule(options().dynamicPort());
	
	private ResourceLoader resourceLoader = new AnnotationConfigServletWebServerApplicationContext();
	private PesquisaElasticSearchImpl repositorio;
	private ConversorEnquete conversor;
	
	private int size = 6;
	private String contexto = "enquetes";
	private String NUMERO_VOTACAO_EM_7_DIAS = "numeroVotacaoEm7Dias";
	
	@Before	
	public void criaRepositorio() {
		this.repositorio = new PesquisaElasticSearchImpl(urlMockElasticSerach(),resourceLoader);
		this.conversor = new ConversorEnquete();
	}
	
	@Test
	public void resultadoDaPesquisa() {
		String tema = "Agropecuária";

		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
			.quantidade(size)
			.ordenacao(singletonList(Ordenacao.builder().campo(NUMERO_VOTACAO_EM_7_DIAS).tipoOrdenacao(TipoOrdenacao.ASC).build()))
			.build();
		parametrosES.criarFiltroContains("temaPortal", tema);

		PesquisaElasticSearch pesquisaMockDeEnquetes = repositorio.pesquisar(parametrosES, contexto);
		List<EnqueteDTO> listaMockDeEnquetes = pesquisaMockDeEnquetes.buscarConteudos(EnqueteDTO.class);
		
		// Lista de ids da consulta
		List<Integer> idsEnquete =  listaMockDeEnquetes.stream().map(EnqueteDTO::getId).collect(Collectors.toList());
				
		assertEquals(Arrays.asList(156851,140466,120203,193500,160894,140214), idsEnquete);
		assertEquals("PRL 1 CAPADR => PL 1384/2011", listaMockDeEnquetes.get(0).getTituloEnquete());
		assertEquals(Integer.valueOf(518663), listaMockDeEnquetes.get(0).getIdProposta());
		assertEquals(Integer.valueOf(556391), listaMockDeEnquetes.get(5).getIdProposta());
		assertEquals("SBT 1 CAPADR => PL 5/2011", listaMockDeEnquetes.get(5).getTituloEnquete());
	}
	
	@Test
	public void conversaoDosDados() {
		String tema = "Agropecuária";
		
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.quantidade(size)
				.ordenacao(singletonList(Ordenacao.builder().campo(NUMERO_VOTACAO_EM_7_DIAS).tipoOrdenacao(TipoOrdenacao.ASC).build()))
				.build();
		parametrosES.criarFiltroContains("temaPortal", tema);

		PesquisaElasticSearch pesquisaMockDeEnquetes = repositorio.pesquisar(parametrosES, contexto);
		List<EnqueteDTO> listaMockDeEnquetes = pesquisaMockDeEnquetes.buscarConteudos(EnqueteDTO.class);
		
		// Convertendo DTO
		List<Enquete> enqueteConvertida = conversor.convert(listaMockDeEnquetes);
		
		// Lista de ids do dominio de conversão
		List<Integer> idsEnquete =  enqueteConvertida.stream().map(Enquete::getId).collect(Collectors.toList());
		
		
		assertEquals(Arrays.asList(156851,140466,120203,193500,160894,140214), idsEnquete);
		assertEquals("PRL 1 CAPADR => PL 1384/2011", enqueteConvertida.get(0).getTitulo());
		assertEquals("/ex/enquetes/518663", enqueteConvertida.get(0).getUrl());
		
		assertEquals("SBT 1 CAPADR => PL 5/2011", enqueteConvertida.get(5).getTitulo());
		assertEquals("/ex/enquetes/556391", enqueteConvertida.get(5).getUrl());
	}
	
	private String[] urlMockElasticSerach() {
		String url = "http://localhost:" + selegWireMockRule.port();
		return new String[]{url};
	}
	
}
