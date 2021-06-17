package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.google.common.base.Strings;

public class FiltroContainsElasticSearch {
	
	public FiltroContainsElasticSearch() {
		this.query = "";
	}
	public FiltroContainsElasticSearch(String parametro, String conteudo) {
		this.query = "";
		and(parametro, conteudo);
	}
	
	private String query;
	
	public JSONObject queryString(OperadorAndOr tipoPesquisaQuery) {
		return new JSONObject().put("query_string", conteudoQueryString(getQuery(), tipoPesquisaQuery.toString()));
	}
	
	private JSONObject conteudoQueryString(String parametrosDaQuery, String tipoAndOr) {
		JSONObject param = new JSONObject();
		param.put("default_operator", tipoAndOr);
		param.put("query", parametrosDaQuery);
		return param;
	}

	public FiltroContainsElasticSearch and(String codigo) {
		adicionarQuery(codigo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch and(String campo, boolean conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch and(String campo, String conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch and(String campo, Double conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch and(String campo, List<?> conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch and(String campo, Integer conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.AND);
		return this;
	}
	
	public FiltroContainsElasticSearch or(String campo, String conteudo) {
		adicionarQuery(campo, conteudo, OperadorAndOr.OR);
		return this;
	}

	public FiltroContainsElasticSearch  or(String codigo) {
		adicionarQuery(codigo, OperadorAndOr.OR);
		return this;
	}
	
	private void adicionarQuery(String codigo, OperadorAndOr operador) {
		if (!Strings.isNullOrEmpty(codigo)) {
			if (!Strings.isNullOrEmpty(query)) {
				query = codigo +" "+operador.toString()+" "+ query;
			}else {
				query = codigo;
			}
		}
	}
	
	private void adicionarQuery(String campo, String conteudo, OperadorAndOr operador) {
		conteudo = escapaCaracatere(conteudo);
		if (!Strings.isNullOrEmpty(campo) && !Strings.isNullOrEmpty(conteudo)) {
			adicionarQuery(campo + ": \"" + conteudo + "\"", operador);
		} else {
			adicionarQuery(conteudo, operador);
		}
	}
	
	private void adicionarQuery(String campo, boolean conteudo, OperadorAndOr operador) {
		if (!Strings.isNullOrEmpty(campo)) {
			adicionarQuery(campo + ": \"" + conteudo + "\"", operador);
		}
	}
	
	/**
	 * Escapa caractere especiais.
	 * Pois alguns caracatere são reservados do ElasticSearch impossibilitando a pesquisa.
	 * Erro retornado caso tente pesquisar um caractere especial é 400
	 * 
	 * @return
	 */
	private String escapaCaracatere(String valor) {
		if (!Strings.isNullOrEmpty(valor) && valor.equals("*")) {
			return valor;
		} else if (!Strings.isNullOrEmpty(valor) &&valor.length() > 0) {
			return valor.replaceAll("r'\\ a.*$'", "\\\\$0");
		}else {
			return valor;
		}
	}
	
	private void adicionarQuery(String campo, Double conteudo, OperadorAndOr operador) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
			if (!Strings.isNullOrEmpty(query)) {
				query = campo + ":" + conteudo +" "+operador.toString()+" "+ query;
			}else {
				query = campo + ":" + conteudo;
			}
		} else if (conteudo != null) {
			query = conteudo.toString();
		}
	}
	
	private void adicionarQuery(String campo, Integer conteudo, OperadorAndOr operador) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
			if (!Strings.isNullOrEmpty(query)) {
				query = campo + ":" + conteudo +" "+operador.toString()+" "+ query;
			}else {
				query = campo + ":" + conteudo;
			}
		} else if (conteudo != null) {
			query = conteudo.toString();
		}
	}
	
	private void adicionarQuery(String campo, List<?> conteudo, OperadorAndOr operador) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null && conteudo.size() > 0) {
			if (!Strings.isNullOrEmpty(query)) {
				String lista =  montarQueryDeLista(campo, conteudo);
				query = lista+ " " + operador.toString() + " " + query;
			}else {
				query = montarQueryDeLista(campo, conteudo);
			}
		}
	}
	private String montarQueryDeLista(String campo, List<?> conteudo) {
		return "(" + conteudo.stream()
				.map(v -> campo + ":\"" + v + "\"")
				.collect(Collectors.joining(" OR ")) + ")";
	}
	
	public String getQuery() {
		if (Strings.isNullOrEmpty(query)) {
			return "*";
		}
		return query;
	}
}
