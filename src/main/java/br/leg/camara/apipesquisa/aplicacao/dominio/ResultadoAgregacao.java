package br.leg.camara.apipesquisa.aplicacao.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@AllArgsConstructor
@Getter
public class ResultadoAgregacao {
	private String nome;
	private Long quantidade;

}
