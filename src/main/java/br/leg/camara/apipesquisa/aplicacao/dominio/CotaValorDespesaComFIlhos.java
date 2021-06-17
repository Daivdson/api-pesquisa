package br.leg.camara.apipesquisa.aplicacao.dominio;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class CotaValorDespesaComFIlhos {
	
	@Builder
	public CotaValorDespesaComFIlhos(CotaValorDespesa despesa, List<CotaValorDespesa> agrergacao) {
		super();
		this.despesa = despesa;
		this.agrergacao = agrergacao;
	}
	private CotaValorDespesa despesa;
	private List<CotaValorDespesa> agrergacao;


}
