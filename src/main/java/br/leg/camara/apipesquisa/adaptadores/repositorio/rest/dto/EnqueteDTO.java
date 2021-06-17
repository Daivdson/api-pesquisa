package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto;

import lombok.Data;

@Data
public class EnqueteDTO {
	private Integer id;
	private Integer idProposta;
	private String tituloEnquete;
	private String url;
	private String descricaoEnquete;
}
