package br.leg.camara.apipesquisa.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.leg.camara.apipesquisa.aplicacao.api.cotas.RepositorioCotas;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.ServicoCotas;
import br.leg.camara.apipesquisa.aplicacao.api.enquete.RepositorioEnquete;
import br.leg.camara.apipesquisa.aplicacao.api.enquete.ServicoEnquete;

@Configuration
public class ServicoConfiguracao {
	
	@Autowired RepositorioEnquete repositorioEnquete;
	
	
	
	@Autowired  RepositorioCotas repositorioCotas;
	
	
	@Bean
	public ServicoEnquete servicoEnquete() {
		return new ServicoEnquete(repositorioEnquete);
	}
	
	@Bean
	public ServicoCotas servicoCotas() {
		return new ServicoCotas(repositorioCotas);
	}
	
}
