package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.common.base.Strings;

public class FiltroTermsElasticSearch {
	
	private List<JSONObject> terms = new ArrayList<JSONObject>();
	
	public List<JSONObject> getTerms(){
		return terms;
	}
	
	public void adicionarTerms(String campo, List<String> conteudo) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
				terms.add(term(campo, conteudo));
		}
	}
	
	private JSONObject term(String campo, Object valor) {
		return new JSONObject().put("terms", conteudoTerms(campo, valor));
	}
	
	private JSONObject conteudoTerms(String campo, Object valor) {
		return new JSONObject().put(campo, valor);
	}
	
}
