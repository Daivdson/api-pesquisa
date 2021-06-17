package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.HashMap;
import static org.apache.commons.lang3.StringUtils.trim;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
public class CotaValorDespesa {
	
	@Builder
	public CotaValorDespesa(String nome, Double valor) {
		super();
		this.nome = trim(nome);
		this.valor = valor;
	}
	
	private String nome;
	private Double valor;
	
	public Map<String, Object> toMap() {
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("nome", this.nome);
		resultado.put("valor", this.valor);
		return resultado;
	}
}
