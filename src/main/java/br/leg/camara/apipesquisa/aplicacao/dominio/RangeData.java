package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RangeData {
	private Date dataInicio;
	private Date dataFim;
	private String campo;
	
	@Builder
	public RangeData(Date dataInicio, Date dataFim, String campo) {
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.campo = campo;
	}
}
