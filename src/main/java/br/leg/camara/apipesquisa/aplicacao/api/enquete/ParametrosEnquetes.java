package br.leg.camara.apipesquisa.aplicacao.api.enquete;

import java.util.ArrayList;
import java.util.List;

import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParametrosEnquetes {
	private Integer pagina;
	private Integer quantidade;
	private String temaPortal;
	private boolean validoPortal;
	private List<Ordenacao> ordenacao;
	
	@Builder
	public ParametrosEnquetes(Integer pagina, Integer quantidade, String temaPortal, boolean validoPortal,
			List<Ordenacao> ordenacao) {
		super();
		this.pagina = pagina == null ? 0 : pagina;
		this.quantidade = quantidade == null ? 20 : quantidade;
		this.temaPortal = temaPortal;
		this.validoPortal = validoPortal;
		this.ordenacao = ordenacao == null ? new ArrayList<>() : ordenacao;
	}

	public Integer getQuantidade() {
		return this.quantidade != null ? this.quantidade : 20;
	}
	
	public Integer getPagina() {
		return this.pagina != null && this.pagina != 0 ? (this.pagina-1)*this.quantidade : 0;
	}
}
