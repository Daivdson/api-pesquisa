package br.leg.camara.apipesquisa.aplicacao.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Enquete {
	
	private Integer id;
	private String descricao;
	private String titulo;
	private String url;

}
