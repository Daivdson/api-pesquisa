package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BucketCotasDTO {

	private String resultado;
	private Long quantidade;
	private Object dados;
	
}
