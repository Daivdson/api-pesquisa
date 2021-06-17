package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AggQueryDTO {

	private String doc_count;
	private Long bg_count;
	private BucketsDTO buckets;
	
}
