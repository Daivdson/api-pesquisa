package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ModoOrdenacao {
	KEY("_key"), COUNT("_count");
	
	@Getter
	private String descricao;

}
