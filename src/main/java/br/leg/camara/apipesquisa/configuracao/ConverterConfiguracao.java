package br.leg.camara.apipesquisa.configuracao;

import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ConverterConfiguracao {
	
	@Bean
	public Converter<String, Ordenacao> conversorOrdenacao() {
		return new Converter<String, Ordenacao>() {

			@Override
			public Ordenacao convert(String source) {
				return new Ordenacao(source);
			}
		};
	}
	
	@Bean
	public Converter<String, List<Ordenacao>> conversorListaOrdenacao() {
		return new Converter<String, List<Ordenacao>>() {

			@Override
			public List<Ordenacao> convert(String source) {
				return Arrays.stream(source.split(",")).map(Ordenacao::new).collect(Collectors.toList());
			}
		};
	}
}
