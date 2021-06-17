package br.leg.camara.apipesquisa.adaptadores.controlador.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LicitacaoDto {
	
	private Integer id;
    private String dataPublicacao;
    private String dataAbertura;
    private String dataJulgamento;
    private Integer ano;
    private String modalidade;
    private Integer numero;
    private String objeto;
    private String situacao;
    private String situacaoSimplificada;
    private Double valorTotal;
    private Integer anoProcesso;
    private Integer numeroProcesso;
    private boolean compraDireta;
    private Double valorTotalAdjudicados;
    

}
