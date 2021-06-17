package br.leg.camara.apipesquisa.aplicacao.dominio;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Ordenacao {
	private String campo;
	private TipoOrdenacao tipoOrdenacao;
	private String campoNested;
	
	public Ordenacao(String campo, TipoOrdenacao tipoOrdenacao) {
		super();
		this.campo = campo != null ? campo : "_key";
		this.tipoOrdenacao = tipoOrdenacao != null ? tipoOrdenacao : TipoOrdenacao.ASC; 
	}

	@Builder
	public Ordenacao(String campo, TipoOrdenacao tipoOrdenacao, String campoNested) {
		this.campo = campo;
		this.tipoOrdenacao = tipoOrdenacao == null ? TipoOrdenacao.ASC : tipoOrdenacao;
		this.campoNested = campoNested;
	}
	
	public Ordenacao(String valor) {
		if (valor == null) throw new NullPointerException();  
		String[] campos = valor.split(":");
		this.campo = campos[0];
		if (campos.length > 1) {
			this.tipoOrdenacao = TipoOrdenacao.valueOf(campos[1].toUpperCase());
		} else {
			this.tipoOrdenacao = TipoOrdenacao.ASC;
		}
	}
}
