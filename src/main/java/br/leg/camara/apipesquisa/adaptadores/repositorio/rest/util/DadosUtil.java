package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util;

import java.util.ArrayList;
import java.util.List;

public class DadosUtil {

	@SuppressWarnings("unchecked")
	public List<String> adaptaObjetoParaLista(Object valor) {
		List<String> lista = new ArrayList<>();
		if (valor instanceof List<?>) {
			lista =  (List<String>) valor;
		}else if(valor != null) {
			lista.add((String) valor);
		}
		return lista;
	}
}

