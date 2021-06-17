package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util.DataUtil;
import br.leg.camara.apipesquisa.aplicacao.dominio.RangeData;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Data
@Slf4j
public class FiltroElasticSearch {

	private static final Object FORMATO_DATA = "dd-MM-YYYY";
	
	public FiltroElasticSearch() {
		this.filtro = new ArrayList<>();
	}
	public FiltroElasticSearch(String parametro, String conteudo) {
		this.filtro = new ArrayList<>();
		add(parametro, conteudo);
	}
	
	private List<JSONObject> filtro;
	
	public FiltroElasticSearch add(String codigo) {
		adicionarJsonAoFiltro(() -> new JSONObject(codigo));
		return this;
	}

	public FiltroElasticSearch add(String campo, String conteudo) {
		if (!Strings.isNullOrEmpty(campo) && !Strings.isNullOrEmpty(conteudo)) {
			adicionarJsonAoFiltro(() -> new JSONObject().put("match_phrase", new JSONObject().put(campo, conteudo)));
		}
		return this;
	}
	
	public FiltroElasticSearch add(String campo, Boolean conteudo) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
			adicionarJsonAoFiltro(() -> new JSONObject().put("match_phrase", new JSONObject().put(campo, conteudo)));
		}
		return this;
	}
	
	public FiltroElasticSearch add(String campo, Integer conteudo) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null) {
			adicionarJsonAoFiltro(() -> new JSONObject().put("match_phrase", new JSONObject().put(campo, conteudo)));
		}
		return this;
	}
	
	public FiltroElasticSearch add(String campo, List<?> conteudo) {
		if (!Strings.isNullOrEmpty(campo) && conteudo != null && conteudo.size() > 0) {
			filtro.addAll(conteudo.stream()
					.map(v -> {
						try {
							return new JSONObject().put("match_phrase", new JSONObject().put(campo, v));
						} catch (JSONException e) {
							log.error("Erro ao add lista de parametros ao filtro ", e);
						}
						return null;
					})
					.collect(toList()));
		}
		return this;
	}

	/**
	 * Pesquisa por range de data (data inicio e data fim)
	 * Caso não seja informado a data fim é considerado a data atual como data fim
	 * @return
	 */
	public FiltroElasticSearch addRangeData(RangeData rangeData) {
		SimpleDateFormat formatar = new SimpleDateFormat(FORMATO_DATA.toString());
		if(rangeData.getDataInicio() != null && !Strings.isNullOrEmpty(rangeData.getCampo())) {
			// Data maior igual que
			String gte =  formatar.format(rangeData.getDataInicio());
			String lte;
			
			if (rangeData.getDataFim() != null) {
				// Data menor igual que
				lte = formatar.format(rangeData.getDataFim());
			} else {
				// Set data atual
				// Set de data atual
				lte = formatar.format(DataUtil.dataAtual());
			}

			adicionarJsonAoFiltro(()  -> {
				JSONObject valores = new JSONObject();
				valores.put("format", FORMATO_DATA);
				valores.put("gte", gte);
				valores.put("lte", lte);
				valores.put("time_zone", "America/Sao_Paulo");
				JSONObject campo = new JSONObject();
				campo.put(rangeData.getCampo(), valores);
				JSONObject range = new JSONObject();
				range.put("range", campo);
			
				return range;
			});
		}
		return this;
	}

	public FiltroElasticSearch addTerms(String campo, Collection<?> valores) {
		if (valores != null && !valores.isEmpty()) {
			adicionarJsonAoFiltro(() -> new JSONObject().put("terms", new JSONObject().put(campo, valores)));
		}
		return this;
	}

	private void adicionarJsonAoFiltro(Supplier<JSONObject> supplier) {
		try {
			JSONObject jsonObject = supplier.get();
			filtro.add(jsonObject);
		} catch (JSONException e) {
			throw new RuntimeException("Ocorreu um erro ao montar filtros para temaplate no ElasticSearch: ", e);
		}
	}
}
