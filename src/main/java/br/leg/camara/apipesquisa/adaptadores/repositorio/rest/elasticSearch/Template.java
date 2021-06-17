package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.ParametrosParaTemplateElasticSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class Template {
	
	private ResourceLoader resourceLoader;
	
	public Template(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader; 
	}
	
	public String configurar(ParametrosParaTemplateElasticSearch parametros, String nomeArquivoJson) {
		try {
			InputStream in = resourceLoader.getResource("classpath:elasticsearch/" + nomeArquivoJson + ".json").getInputStream();
			String template = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
						
			String filtros = "\"must\":"+parametros.getMust()+","+
							 "\"filter\":"+parametros.getFilter()+","+
							 "\"must_not\":"+parametros.getMustNot();

			return String.format(
					template,
					parametros.getFrom(),
					parametros.getSize(),
					parametros.getSort(),
					parametros.getSource(),
					filtros,
					parametros.getAgregacao());
		} catch (Exception e) {
			log.error("Erro ao montar o template com parametros para elasticSearch", e);
			return null;
		}
	}
	
	// Parametros do query -> bool personalizados 
	public String configurar(ParametrosParaTemplateElasticSearch parametros, String nomeArquivo, String parametrosQueryPersonalizados) {
		try {
			InputStream in = resourceLoader.getResource("classpath:elasticsearch/" + nomeArquivo + ".json").getInputStream();
			String template = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

			return String.format(
					template,
					parametros.getFrom(),
					parametros.getSize(),
					parametros.getSort(),
					parametros.getSource(),
					parametrosQueryPersonalizados,
					parametros.getAgregacao());
		} catch (Exception e) {
			log.error("Erro ao montar o template com parametros para elasticSearch", e);
			return null;
		}
	}

}
