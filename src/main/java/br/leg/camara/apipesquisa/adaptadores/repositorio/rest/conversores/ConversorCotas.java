package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.leg.camara.apipesquisa.aplicacao.dominio.CotaValorDespesa;

public class ConversorCotas {

	public List<Map<String, Object>> converterBuketsDe2Niveis(JsonArray bucketsDoNivel1E2, String chaveSegundoNivel){
		List<Map<String, Object>> bucketsDoNivel1E2Convertidos = new ArrayList<Map<String,Object>>();
		
		bucketsDoNivel1E2.forEach(v -> {
			Map<String, Object> resultado = new HashMap<String, Object>();
			resultado.putAll(converterObjBuketsDeSoma(v.getAsJsonObject()).toMap());
			resultado.put(chaveSegundoNivel, converterBuketsDe1Nivel(
							v.getAsJsonObject().getAsJsonObject(chaveSegundoNivel).getAsJsonArray("buckets")));
					
			bucketsDoNivel1E2Convertidos.add(resultado); 
		});
		return bucketsDoNivel1E2Convertidos;
	}
	
	public List<CotaValorDespesa>  converterBuketsDe1Nivel(JsonArray buckets){
		List<CotaValorDespesa> bucketsConvertidos = new ArrayList<CotaValorDespesa>();
		buckets.forEach(v -> {
			bucketsConvertidos.add(converterObjBuketsDeSoma(v.getAsJsonObject()));
		});
		return bucketsConvertidos;
	}
	
	public CotaValorDespesa converterObjBuketsDeSoma(JsonObject jsonValor){
		return CotaValorDespesa.builder()
				.nome(jsonValor.get("key").getAsString())
				.valor(jsonValor.getAsJsonObject().getAsJsonObject("valor").get("value").getAsDouble())
				.build();
	}
	
}
