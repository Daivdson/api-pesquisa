package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util;

public class Util {
	
	public String tratarParametroFaixaValor(String faixaValor) {
		if (faixaValor == null) return null;
		switch (faixaValor) {
		case "ate176mil":
			return "Até R$ 176.000";
		case "176mila330mil":
			return "De R$ 176.000 a R$ 330.000";
		case "330mila1430mil":
			return "De R$ 330.000 a R$ 1.430.000";
		case "1430mila3300mil":
			return "De R$ 1.430.000 a R$ 3.300.000";
		case "acima3300mil":
			return "Acima de R$ 3.300.000";
		default:
			return null;
		}
	}

}
