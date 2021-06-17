package br.leg.camara.apipesquisa.configuracao;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

public class CacheConfig {
	
	private static final String CACHE = "public, max-age=%d";
	
	public static void setDuracao(HttpServletResponse response, int duracaoCacheEmSegundos) {
		response.setHeader(HttpHeaders.CACHE_CONTROL, String.format(CACHE, duracaoCacheEmSegundos));
	}
	
}
