package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

import lombok.Getter;

@Getter
public class FiltroExcludenteElasticSearch {

	public FiltroExcludenteElasticSearch() {
		this.filtro = new ArrayList<>();
	}
	public FiltroExcludenteElasticSearch(String parametro, String conteudo) {
		this.filtro = new ArrayList<>();
		add(parametro, conteudo);
	}
	
	private List<JSONObject> filtro;

	public FiltroExcludenteElasticSearch add(String campo, String conteudo) {
		try {
			if (!Strings.isNullOrEmpty(campo) && !Strings.isNullOrEmpty(conteudo)) {
				filtro.add(new JSONObject().put(campo, conteudo));
			}
		} catch (JSONException e) {
			throw new RuntimeException("Ocorreu um erro ao montar filtros excludente para temaplate no ElasticSearch: ", e);
		}
		return this;
	}
	
	public FiltroExcludenteElasticSearch add(String campo, Integer conteudo) {
		try {
			if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
				filtro.add(new JSONObject().put(campo, conteudo));
			}
		} catch (JSONException e) {
			throw new RuntimeException("Ocorreu um erro ao montar filtros excludente para temaplate no ElasticSearch: ", e);
		}
		return this;
	}
}
