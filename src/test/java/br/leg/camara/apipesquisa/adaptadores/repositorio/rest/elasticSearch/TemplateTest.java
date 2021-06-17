package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.OperadorAndOr;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;


@SpringBootTest
public class TemplateTest {
	
	private Template template;
	private ResourceLoader resourceLoader = new AnnotationConfigServletWebServerApplicationContext();
	
	@Before
	public void parametrosElasticSearch() {
		this.template = new Template(resourceLoader);
	}

	@Test
	public void templatePesquisaBibliotecaDigital() {
		String contexto = "bibliotecadigital";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(3)
				.ordenacao(singletonList(Ordenacao.builder().campo("dataOrdenacao").tipoOrdenacao(TipoOrdenacao.ASC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltroContains("temaPortal", "Agropecuária");
		parametrosES.criarFiltro("colecao", "Comiss\\\\ões");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 3"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"dataOrdenacao\":{\"order\":\"ASC\"}}]"));
		assertTrue(templateMonstado.contains("{\"query_string\":{\"default_operator\":\"AND\",\"query\":\"temaPortal: \\\"Agropecuária\\\"\"}}"));
	}
	
	@Test
	public void templatePesquisaBibliotecaDiscurso() {
		String contexto = "discursos";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(3)
				.ordenacao(singletonList(Ordenacao.builder().campo("dataOrdenacao").tipoOrdenacao(TipoOrdenacao.ASC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltroContains("temaPortal", "Direitos humanos");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 3"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"dataOrdenacao\":{\"order\":\"ASC\"}}]"));
		assertTrue(templateMonstado.contains("\"query\":\"temaPortal: \\\"Direitos humanos\\\"\""));
	}
	
	@Test
	public void templatePesquisaEnquete() {
		String contexto = "enquetes";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(6)
				.ordenacao(singletonList(Ordenacao.builder().campo("numeroVotacaoEm7Dias").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltroContains("temaPortal", "Agropecuária");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 6"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"numeroVotacaoEm7Dias\":{\"order\":\"DESC\"}}]"));
		assertTrue(templateMonstado.contains("{\"query_string\":{\"default_operator\":\"AND\",\"query\":\"temaPortal: \\\"Agropecuária\\\"\"}}"));
	}
	
	@Test
	public void templatePesquisaGenerico() {
		String contexto = "generico";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(6)
				.ordenacao(singletonList(Ordenacao.builder().campo("pageviews").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltroContains("temaPortal", "Agropecuária");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 6"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"pageviews\":{\"order\":\"DESC\"}}]"));
		assertTrue(templateMonstado.contains("{\"query_string\":{\"default_operator\":\"AND\",\"query\":\"temaPortal: \\\"Agropecuária\\\"\"}}"));
	}
	
	@Test
	public void templatePesquisaNoticias() {
		String contexto = "noticias";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(20)
				.ordenacao(singletonList(Ordenacao.builder().campo("dataOrdenacao").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltro("tags", "plenário");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 20"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"dataOrdenacao\":{\"order\":\"DESC\"}}]"));
		assertTrue(templateMonstado.contains("\"query\":\"*\""));
		assertTrue(templateMonstado.contains("{\"match_phrase\":{\"tags\":\"plenário\"}}"));
	}
	
	@Test
	public void templatePesquisaPortal() {
		String contexto = "portalcamara";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.pagina(1)
				.quantidade(10)
				.ordenacao(singletonList(Ordenacao.builder().campo("dataOrdenacao").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.tipoPesquisaQuery(OperadorAndOr.AND)
				.build();
		parametrosES.criarFiltroContains("temaOriginal","Agropecuária");
		parametrosES.criarFiltro("colecao", "Comissões")
			.add("review_state", "published");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 10"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"dataOrdenacao\":{\"order\":\"DESC\"}}]"));
		assertTrue(templateMonstado.contains("{\"query_string\":{\"default_operator\":\"AND\",\"query\":\"temaOriginal: \\\"Agropecuária\\\"\"}}"));
	}
	
	@Test
	public void templatePesquisaVotacaoAprovadas() {
		String contexto = "portalcamara";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.ordenacao(singletonList(Ordenacao.builder().campo("_score").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.quantidade(1)
				.camposASeremExcluidos(new String[] {"inteiroTeor","tramitacoes","referencias","arvore","emendas","emendasSubstitutivo"})
				.build();
		parametrosES.criarFiltroContains("orgaoAprovacao", "CDEICS")
		.and("anoAprovacao", 2018);

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 1"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"_score\":{\"order\":\"DESC\"}}]"));
		assertTrue(templateMonstado.contains("\"query\":\"anoAprovacao:2018 AND orgaoAprovacao: \\\"CDEICS\\\"\""));
	}
	
	@Test
	public void templatePesquisaVotacaoReprovadas() {
		String contexto = "proposicoes";
		ParametrosParaTemplateElasticSearch parametrosES = ParametrosParaTemplateElasticSearch.builder()
				.ordenacao(singletonList(Ordenacao.builder().campo("_score").tipoOrdenacao(TipoOrdenacao.DESC).build()))
				.quantidade(1)
				.camposASeremExcluidos(new String[] {"inteiroTeor","tramitacoes","referencias","arvore","emendas","emendasSubstitutivo"})
				.build();
		parametrosES.criarFiltroContains("orgaoRejeicao", "CDEICS")
		.and("anoRejeicao", "2018");

		String templateMonstado = template.configurar(parametrosES, contexto); 
		
		assertTrue(templateMonstado.contains("\"from\" : 0, \"size\" : 1"));
		assertTrue(templateMonstado.contains("\"sort\" : [{\"_score\":{\"order\":\"DESC\"}}]"));
	}
}
