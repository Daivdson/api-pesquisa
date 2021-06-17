package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util;

import java.text.Normalizer;

public class StringsApiPesquisaUtils {
	
    public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

}
