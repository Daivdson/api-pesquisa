package br.leg.camara.apipesquisa.aplicacao.api.cotas;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util.DataUtil;
import br.leg.camara.apipesquisa.aplicacao.enums.Uf;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ParametrosCota {
	
	private final static String PATTERN = "MM-YYYY";
	private Integer pagina;
	private Integer quantidade;
	private Integer quantidadeNivel1;
	private Integer quantidadeNivel2;
	private TipoOrdenacao ordenacao = TipoOrdenacao.DESC;
	private String geral;
	private String mesAno;
	private String mesAnoInicial;
	private String mesAnoFinal;
	private String siglaPartido;
	private String tipoDespesa;
	private Uf uf;
	private boolean pesquisaPorDeputado;
	private boolean pesquisaPorLideranca;
	private List<String> deputado;
	private List<String> lideranca;
	private List<String> nomeFornecedorCpfCnpj;
	private List<String> deputadoPartidoUfId;
	private List<String> tipoDespesas;
	private String numeroDocumento;
	private String cnpjCpfFornecedor;
	
	public Date getDataInicio() {
		if(this.mesAnoInicial != null) {
			String data = "1-"+this.mesAnoInicial;
			return DataUtil.conversorData(data);
		}
		return null;
	}
	
	public Date getDataFim() {
		if(this.mesAnoFinal != null) {
			Date dataMesAnoFinalConvertida = DataUtil.conversorData(this.mesAnoFinal,PATTERN);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataMesAnoFinalConvertida);
			int ultimoDiaMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			String dataUltimoDiaMes = ultimoDiaMes+"-"+this.mesAnoFinal;
			
			Date dataConvertida = DataUtil.conversorData(dataUltimoDiaMes);
			
			return dataConvertida;
		}
		return null;
	}
	
	public TipoSolicitante getTipoSolicitante() {
		if(isPesquisaPorDeputado()) {
			return TipoSolicitante.deputado;
		} else if(isPesquisaPorLideranca()) {
			return TipoSolicitante.lideranca;
		} else {
			log.warn("Parametro de pesquisaPorDeputado ou pesquisaPorLideranca não foi informado");
			return null;
		} 
	}

	public List<String> getNomeFornecedorCpfCnpj() {
		List<String> fornecedor = this.nomeFornecedorCpfCnpj;
		if(fornecedor == null)
			return fornecedor;
		return fornecedor.stream()
				.map(v -> v.replace("_", "#"))
				.collect(Collectors.toList());
	}
	
	public List<String> getDeputadoPartidoUfId() {
		List<String> nomeCompostoDeputado = this.deputadoPartidoUfId;
		if(nomeCompostoDeputado == null)
			return nomeCompostoDeputado;
		return nomeCompostoDeputado.stream()
				.map(v -> v.replace("_", "#"))
				.collect(Collectors.toList());
	}
	
	@Builder
	public ParametrosCota(Integer pagina, Integer quantidade, TipoOrdenacao ordenacao, String geral,
			String mesAno, String mesAnoInicial, String mesAnoFinal, String siglaPartido,
			String tipoDespesa, Uf uf, boolean pesquisaPorDeputado, boolean pesquisaPorLideranca, List<String> deputado,
			List<String> lideranca, Integer quantidadeNivel1, Integer quantidadeNivel2, List<String> nomeFornecedorCpfCnpj, 
			List<String> deputadoPartidoUfId, List<String> tipoDespesas, String numeroDocumento, String cnpjCpfFornecedor) {
		super();
		this.pagina = pagina == null ? 1 : pagina;
		this.quantidadeNivel1 = quantidadeNivel1 == null ? 50 : quantidadeNivel1;
		this.quantidadeNivel2 = quantidadeNivel2 == null ? 20 : quantidadeNivel2;
		this.quantidade = quantidade == null ? 20 : quantidade;
		this.ordenacao = ordenacao == null ? TipoOrdenacao.DESC : ordenacao; 
		
		this.deputadoPartidoUfId = deputadoPartidoUfId;
		this.nomeFornecedorCpfCnpj = nomeFornecedorCpfCnpj;
		this.geral = geral;
		this.mesAno = mesAno != null ? mesAno.substring(0, 1).equals("0") ? mesAno.substring(1) : mesAno : mesAno;
		this.mesAnoInicial = mesAnoInicial;
		this.mesAnoFinal = mesAnoFinal;
		this.siglaPartido = siglaPartido;
		this.tipoDespesa = tipoDespesa;
		this.uf = uf;
		this.pesquisaPorDeputado = pesquisaPorDeputado;
		this.pesquisaPorLideranca = pesquisaPorLideranca;
		this.deputado = deputado;
		this.lideranca = lideranca;
		this.tipoDespesas = tipoDespesas;
		this.numeroDocumento = numeroDocumento;
		this.cnpjCpfFornecedor = cnpjCpfFornecedor;
	}
	
}
