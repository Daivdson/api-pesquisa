package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AgregacaoBucketElasticSearch {
	
	public AgregacaoBucketElasticSearch() {
		this.agregacao = new ArrayList<>();
	}

	@Builder
	public AgregacaoBucketElasticSearch(String nome, String campo, int quatidadeBucket, TipoOrdenacao ordem, @NonNull ModoOrdenacao modo) {
		this.agregacao = new ArrayList<>();
		add(nome, campo, quatidadeBucket, ordem, modo);
	}

	private List<String> agregacao;
	
	/**
	 * Agregacao 
	 * @param nome da agregação
	 * @param campo que deve ser agregado
	 * @param quatidadeBucket quantos bucket deve retorna
	 * @param ordem do resutado
	 * @param modo que o resultado deve retorna
	 */
	public void add(String nome, String campo, int quatidadeBucket, @NonNull TipoOrdenacao ordem, @NonNull ModoOrdenacao modo) {
		required(nome, "Nome é obrigatório para uma agregacao");
		required(campo, "campo no indice é obrigatório");

		agregacao.add("\"" + nome + "\": {\n" + 
				"        	\"terms\": {\n" + 
				"            	\"field\": \"" + campo + "\",\n" + 
				"					\"size\": " + quatidadeBucket + ",\n" + 
				"					\"order\" : { \"" + modo.getDescricao() + "\" : \"" + ordem.toString().toLowerCase() + "\" }\n" + 
				"         	}		\n" + 
				"    	}");
		
	}
	
	public void add(String nome, String campo, String expressao) {
		required(nome, "Nome é obrigatório para uma agregacao");
		required(campo, "campo no indice é obrigatório");
		required(expressao, "Expressão da agregação é obrigatório");

		agregacao.add("\""+nome+"\": {\r\n" + 
				"    		\"significant_terms\": {\r\n" + 
				"    			\"field\": \""+campo+"\",\r\n" + 
				"    			\"background_filter\":{\r\n" + 
				"    				\"query_string\":{\r\n" + 
				"    					\"query\":\""+expressao+"\"\r\n" + 
				"    				}\r\n" + 
				"    				\r\n" + 
				"    			}\r\n" + 
				"    		}\r\n" + 
				"    	}");
		
	}
	
	public void add(String codigoAgregacao) {
		agregacao.add(codigoAgregacao);
	}
	
	private void required(String valor, String mensagem) {
		if (Strings.isNullOrEmpty(valor)) throw new NullPointerException(mensagem);
	}
	
	@Override
	public String toString() {
		return String.join(",", agregacao);
	}
}
