package br.leg.camara.apipesquisa.adaptadores.controlador.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResultadoDto<T> {
	private Long quantidadeTotal;
	private List<T> resultado;
	private Map<String, Object> agregacao;
}
