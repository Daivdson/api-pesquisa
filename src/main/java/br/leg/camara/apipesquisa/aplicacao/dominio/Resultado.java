package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Resultado<T> {
	private Long quantidadeTotal;
	private List<T> resultado;
	private Map<String, Object> agregacao;
	
	public Resultado(Long quantidadeTotal, List<T> resultado) {
		this.quantidadeTotal = quantidadeTotal;
		this.resultado = resultado;
		this.agregacao = new HashMap<>();
	}
	
	public Resultado(Long quantidadeTotal, List<T> resultado, Map<String, Object> agregacao) {
		this.quantidadeTotal = quantidadeTotal;
		this.resultado = resultado;
		this.agregacao = agregacao;
	}
}
