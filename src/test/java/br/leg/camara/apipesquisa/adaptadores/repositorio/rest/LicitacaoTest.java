package br.leg.camara.apipesquisa.adaptadores.repositorio.rest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.PesquisaElasticSearchImpl;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.ParametrosLicitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Licitacao;

@SpringBootTest
public class LicitacaoTest {
	
	@Rule
	public WireMockRule selegWireMockRule = new WireMockRule(options().dynamicPort());
	
	private ResourceLoader resourceLoader = new AnnotationConfigServletWebServerApplicationContext();
	
	private PesquisaElasticSearchImpl repositorio;
	private ParametrosLicitacao parametros;
	private String contexto = "licitacoes";
	
	@Before	
	public void criaRepositorio() {
		this.repositorio = new PesquisaElasticSearchImpl(urlMockElasticSerach(),resourceLoader);
		this.parametros = ParametrosLicitacao.builder()
				.geral("Manutenção preventiva e corretiva nas instalações")
				.quantidade(3)
				.build();
	}
	
	@Test
	public void resultadoDaPesquisa() {

		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(parametros.getPagina())
				.quantidade(parametros.getQuantidade())
				.build();
				
		parametrosES.criarFiltroContains("situacao", parametros.getSituacao())
		.and("", parametros.getGeral());
		parametrosES.criarFiltro("ano", parametros.getAno());
		
		PesquisaElasticSearch pesquisaMockDeDiscursos = repositorio.pesquisar(parametrosES,contexto);
		List<Licitacao> listaMockDeLicitacao = pesquisaMockDeDiscursos.buscarConteudos(Licitacao.class);
		
		// Lista de ids da consulta
		List<Integer> idsLicitacoes =  listaMockDeLicitacao.stream().map(Licitacao::getId).collect(Collectors.toList());
				
		assertEquals(Arrays.asList(4937,155,108), idsLicitacoes);
		assertEquals("Julgada", listaMockDeLicitacao.get(0).getSituacao());
		assertEquals(9022, listaMockDeLicitacao.get(0).getArquivos().get(0).getId().intValue());
	}
	
	@Test
	public void conversaoDosDados() {

		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(parametros.getPagina())
				.quantidade(parametros.getQuantidade())
				.build();
				
		parametrosES.criarFiltroContains("situacao", parametros.getSituacao())
		.and("", parametros.getGeral());
		parametrosES.criarFiltro("ano", parametros.getAno());
		
		PesquisaElasticSearch pesquisaMockDeDiscursos = repositorio.pesquisar(parametrosES,contexto);
		List<Licitacao> listaMockDeLicitacao = pesquisaMockDeDiscursos.buscarConteudos(Licitacao.class);
		
		// Convertendo DTO
		List<Licitacao> licitacaoConvertida = listaMockDeLicitacao;
		
		// Lista de ids do dominio de conversão
		List<Integer> idsLicitacao =  licitacaoConvertida.stream().map(Licitacao::getId).collect(Collectors.toList());
		
		assertEquals(Arrays.asList(4937,155,108), idsLicitacao);
		assertEquals("Dispensa de Licitação              ", licitacaoConvertida.get(2).getModalidade());
		assertEquals(425, licitacaoConvertida.get(2).getItens().get(0).getId().intValue());
		assertEquals("00.663.310/0001-96", licitacaoConvertida.get(2).getItensAdjudicado().get(0).getCpfCnpjFornecedor());
	}
	
	private String[] urlMockElasticSerach() {
		String url = "http://localhost:" + selegWireMockRule.port();
		return new String[]{url};
	}
	
}
