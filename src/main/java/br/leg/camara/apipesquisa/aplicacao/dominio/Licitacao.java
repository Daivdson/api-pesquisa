package br.leg.camara.apipesquisa.aplicacao.dominio;

import static org.apache.commons.lang3.StringUtils.trim;

import java.util.List;

import lombok.Getter;

@Getter
public class Licitacao {
	
	private Integer id;
	private Integer numero;                        
    private Double valorTotal;
    private String dataJulgamento;
    private String dataPublicacao;
    private Integer ano;
    private Integer anoProcesso;
    private Integer numeroProcesso;
    private String modalidade;
    private String tipo;
    private String situacao;
    private String situacaoSimplificada;
	private List<LicitacaoArquivos> arquivos;
	private List<LicitacaoItens> itens;
	private List<LicitacaoGrupoItensAdjudicado> itensAdjudicado;
	private String dataAbertura;
	private String objeto;
	private Boolean compraDireta;
	private Double valorTotalAdjudicados;
	private String numeroAno;
	
	
	public Licitacao(Integer id, Integer numero, Double valorTotal, String dataJulgamento, String dataPublicacao, Integer ano, Integer anoProcesso,
			Integer numeroProcesso, String modalidade, String tipo, String situacao, String situacaoSimplificada, List<LicitacaoArquivos> arquivos, List<LicitacaoItens> itens, 
			List<LicitacaoGrupoItensAdjudicado> itensAdjudicado, String dataAbertura, String objeto, Boolean compraDireta, Double valorTotalAdjudicados, String numeroAno) {		
		this.id = id;
		this.numero = numero;
		this.valorTotal = valorTotal;
		this.dataJulgamento = dataJulgamento;
		this.dataPublicacao = dataPublicacao;
		this.ano = ano;
		this.anoProcesso = anoProcesso;
		this.numeroProcesso = numeroProcesso;
		this.modalidade = trim(modalidade);
		this.tipo = trim(tipo);
		this.situacao = situacao;
		this.situacaoSimplificada = situacaoSimplificada;
		this.arquivos = arquivos;
		this.itens = itens;
		this.itensAdjudicado = itensAdjudicado;
		this.dataAbertura = dataAbertura;
		this.objeto = objeto;
		this.compraDireta = compraDireta;
		this.valorTotalAdjudicados = valorTotalAdjudicados;
		this.numeroAno = numeroAno;
	}
}
