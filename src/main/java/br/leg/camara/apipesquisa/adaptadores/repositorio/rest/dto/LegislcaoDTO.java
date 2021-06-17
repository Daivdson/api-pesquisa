package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class LegislcaoDTO {
	private String texOrigens;
    private String texDispositivoApreciacao;
    private String texColecao;
    private String texIndexacao;
    private String ideNumeroCongresso;
    private Integer anoNorma;
    private String dataOrdenacao;
    private String nomTipoNorma;
    private String nomOrigemNorma;
    private String datAtualizacaoTexto;
    private String indCriadoRefRecebe;
    private String texEpigrafe;
    private String sigTipoNorma;
    private Integer id;
    private String texNumeroNorma;
    private String texEpigrafeReferencia;
    private String texPrimeiraEmenta;
    private Integer anoCongresso;
    private String texSiglasOrigens;
    private Integer anoPresidencial;
    private String sigOrigemNorma;
    private String sigColecaoNorma;
    private String datAssinatura;
    private String datCadastro;
    private String nomColecaoNorma;
    private String ideNumeroPresidencial;
    private String texQualificacaoVeto;
    private String url;
    private String textoAtualizadoHtml;
    private Integer idTema;
    private String temaOriginal;
    private Integer pageviews;
    private String nomSituacaoNorma;
    private List<String> temaPortal;
}
