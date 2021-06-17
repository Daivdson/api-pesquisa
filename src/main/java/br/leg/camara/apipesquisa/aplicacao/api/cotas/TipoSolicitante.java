package br.leg.camara.apipesquisa.aplicacao.api.cotas;

public enum TipoSolicitante {

	deputado("Deputado"), lideranca("Liderança");

	
	TipoSolicitante(String value) {
		this.value = value;
	}

	private String value;
	
	public String getValue() {
		return this.value;
	}
}
