package br.leg.camara.apipesquisa.adaptadores.controlador.dto;

import java.util.List;

import lombok.Data;

@Data
public class ParametroCotasSimplificadoDTO {
	
	// padrão 22-2020
	private String mesAnoInicial;
	// padrão 22-2020
	private String mesAnoFinal;
	private String campoOrdenacao;
	private Integer quantidade;
	private List<String> deputadoPartidoUfId;
	private List<String> liderancas;
	private List<String> nomeFornecedorCpfCnpj;
	private List<String> tipoDespesas;
	private String numeroDocumento;
	private String cnpjCpfFornecedor;
}
