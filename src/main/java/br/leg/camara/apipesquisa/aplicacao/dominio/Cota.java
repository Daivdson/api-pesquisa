package br.leg.camara.apipesquisa.aplicacao.dominio;
import static org.apache.commons.lang3.StringUtils.trim;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Cota {
	
	private String nomePassageiro;
    private String mesAno;
    private String data;
    private Integer idTipoDespesa;
    private String nomeParlamentar;
    private Integer mes;
    private String numeroDocumento;
    private String id;
    private String tipoDespesa;
    private Double valorReembolso;
    private String siglaUF;
    private Integer ano;
    private String tipoSolicitante;
    private Double valorRestituicao;
    private Integer idParlamentar;
    private String deputado;
    private String lideranca;
    private String tipoDocumento;
    private Double valorDespesa;
    private String nomeFornecedor;
    private Integer idDocumento;
    private String siglaPartido;
    private String urlDocumento;
    private String tipoFornecedor;
    private Integer idSolicitante;
    private String cnpjCpfFornecedor;
    
    @Builder
	public Cota(String nomePassageiro, String mesAno, String data, Integer idTipoDespesa, String nomeParlamentar,
			Integer mes, String numeroDocumento, String id, String tipoDespesa, Double valorReembolso, String siglaUF,
			Integer ano, String tipoSolicitante, Double valorRestituicao, Integer idParlamentar, String deputado,
			String lideranca, String tipoDocumento, Double valorDespesa, String nomeFornecedor, Integer idDocumento,
			String siglaPartido, String urlDocumento, String tipoFornecedor, Integer idSolicitante,
			String cnpjCpfFornecedor) {
		super();
		this.nomePassageiro = nomePassageiro;
		this.mesAno = mesAno;
		this.data = data;
		this.idTipoDespesa = idTipoDespesa;
		this.nomeParlamentar = trim(nomeParlamentar);
		this.mes = mes;
		this.numeroDocumento = numeroDocumento;
		this.id = id;
		this.tipoDespesa = tipoDespesa;
		this.valorReembolso = valorReembolso;
		this.siglaUF = siglaUF;
		this.ano = ano;
		this.tipoSolicitante = tipoSolicitante;
		this.valorRestituicao = valorRestituicao;
		this.idParlamentar = idParlamentar;
		this.deputado = trim(deputado);
		this.lideranca = trim(lideranca);
		this.tipoDocumento = tipoDocumento;
		this.valorDespesa = valorDespesa;
		this.nomeFornecedor = nomeFornecedor;
		this.idDocumento = idDocumento;
		this.siglaPartido = siglaPartido;
		this.urlDocumento = urlDocumento;
		this.tipoFornecedor = tipoFornecedor;
		this.idSolicitante = idSolicitante;
		this.cnpjCpfFornecedor = cnpjCpfFornecedor;
	}
    
    
}
