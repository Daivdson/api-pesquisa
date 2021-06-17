package br.leg.camara.apipesquisa.adaptadores.controlador;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.leg.camara.apipesquisa.adaptadores.controlador.dto.ParametroCotasDTO;
import br.leg.camara.apipesquisa.adaptadores.controlador.dto.ParametroCotasSimplificadoDTO;
import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros.TipoOrdenacao;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.ParametrosCota;
import br.leg.camara.apipesquisa.aplicacao.api.cotas.ServicoCotas;
import br.leg.camara.apipesquisa.aplicacao.dominio.Cota;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

@RestController
@RequestMapping(value = "/api/cotas" , produces = {MediaType.APPLICATION_JSON_VALUE})
public class CotasCtrlAPI {

	private final ServicoCotas servicoCotas;
	private final static String despesaUrl = "/despesa";
	private final static String deputadoUrl = "/deputado";
	private final static String liderancaUrl = "/lideranca";
	private final static String fornecedorUrl = "/fornecedor";
	private static String keyword = ".keyword";
	
	private static String ROTULO_LIDERANCA = "lideranca";
	private static String CAMPO_LIDERANCA = "lideranca".concat(keyword);
	
	private static String ROTULO_DEPUTADO = "deputado";
	private static String CAMPO_DEPUTADO = "deputadoUfId";
	
	private static String ROTULO_TIPO_DESPESA = "tipoDespesa";
	private static String CAMPO_TIPO_DESPESA = "tipoDespesa".concat(keyword);
	
	private static String ROTULO_MES_ANO = "mesAno";
	private static String CAMPO_MES_ANO = "mesAno";
	
	private static String ROTULO_FORNECEDOR = "fornecedor";
	private static String CAMPO_FORNECEDOR = "nomeFornecedorCpfCnpj";

	public CotasCtrlAPI(ServicoCotas servicoCotas) {
		this.servicoCotas = servicoCotas;
	}

	@GetMapping(value = { "", "/" })
	public Resultado<Cota> gelral(ParametroCotasDTO filtro) {
		Resultado<Cota> resultado = servicoCotas.listaGeral(converterDtoparametros(filtro));
		return resultado;
	}
	
	/**
	 * Parâmetros: 
	 * campoOrdenacao=valor
	 * mesAnoFinal=MM-YYYY
	 * mesAnoInicial=MM-YYYY
	**/
	
	@GetMapping(value = { despesaUrl})
	public Map<String, Object> tipoDespesa(
			@RequestParam(name="mesAnoInicial", required = false) String mesAnoInicial,
			@RequestParam(name="mesAnoFinal", required = false) String mesAnoFinal,
			@RequestParam(name="geral", required = false) String geral
			) {
		ParametrosCota param = ParametrosCota.builder()
				.mesAnoFinal(mesAnoFinal)
				.mesAnoInicial(mesAnoInicial)
				.geral(geral)
				.build();
		Map<String, Object> resultado = servicoCotas.agregacao(param, "tipoDespesa","tipoDespesa"+keyword);
		return resultado;
	}
	
	// TIPO DEPESA :: DEPUTADOS

	@GetMapping(value = { despesaUrl+"/deputados"})
	public Map<String, Object> tipoDespesaDeputado(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.tipoDespesas(filtro.getTipoDespesas())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_TIPO_DESPESA, CAMPO_TIPO_DESPESA, ROTULO_DEPUTADO, CAMPO_DEPUTADO, ordenacao);
		return resultado;
	}

	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/deputados/{deputado}"})
	public Map<String, Object> tipoDespesaDeputadoMesAno(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("deputado") String deputado, 
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorDeputado(true)
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return  servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_MES_ANO, CAMPO_MES_ANO);
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/deputados/{deputado}/{mesAno}"})
	public Resultado<Cota> despesaDeputadoPorTipo(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("mesAno") String mesAno,
			@PathVariable("deputado") String nomeDeputado,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.deputadoPartidoUfId(Arrays.asList(nomeDeputado))
				.mesAno(mesAno)
				.quantidade(filtro.getQuantidade())
				.build();
		Resultado<Cota> resultado = servicoCotas.cotaDe0Nivel(parametros);
		return new Resultado<Cota>(resultado.getQuantidadeTotal(), resultado.getResultado(), resultado.getAgregacao());
	}
	
	// TIPO DEPESA :: LIDERANCAS 
	
	@GetMapping(value = { despesaUrl+"/liderancas"})
	public Map<String, Object> tipoDespesaLideranca(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.tipoDespesas(filtro.getTipoDespesas())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_TIPO_DESPESA, CAMPO_TIPO_DESPESA, ROTULO_LIDERANCA, CAMPO_LIDERANCA, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/liderancas/{lideranca}"})
	public Map<String, Object> tipoDespesaLiderancasMesAno(@PathVariable("tipoDespesa") String tipoDespesa,
			@PathVariable("lideranca") String lideranca, 
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorLideranca(true)
				.lideranca(Arrays.asList(lideranca))
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_MES_ANO, CAMPO_MES_ANO);
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/liderancas/{lideranca}/{mesAno}"})
	public Resultado<Cota> despesaLiderancaPorTipo(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("mesAno") String mesAno,
			@PathVariable("lideranca") String lideranca,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.mesAno(mesAno)
				.lideranca(Arrays.asList(lideranca))
				.quantidade(filtro.getQuantidade())
				.build();
		Resultado<Cota> resultado = servicoCotas.cotaDe0Nivel(parametros);

		return new Resultado<Cota>(resultado.getQuantidadeTotal(), resultado.getResultado(), resultado.getAgregacao());
	}
	
	// TIPO DEPESA :: GASTOS MENSAIS
	
	
	@GetMapping(value = { despesaUrl+"/gastos-mensais"})
	public Map<String, Object> tipoDespesaGastosMensais(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidade(filtro.getQuantidade())
				.quantidadeNivel1(filtro.getQuantidade())
				.tipoDespesas(filtro.getTipoDespesas())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_TIPO_DESPESA, CAMPO_TIPO_DESPESA, ROTULO_MES_ANO, CAMPO_MES_ANO, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/gastos-mensais/{mesAno}/deputados"})
	public Map<String, Object> tipoDespesaGastosMensaisDeputado(@PathVariable("tipoDespesa") String tipoDespesa,
			@PathVariable("mesAno") String mesAno, 
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorDeputado(true)
				.mesAno(mesAno)
				.quantidadeNivel1(filtro.getQuantidade())
				.build(); 
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_DEPUTADO, CAMPO_DEPUTADO);
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/gastos-mensais/{mesAno}/liderancas"})
	public Map<String, Object> tipoDespesaGastosMensaisLideranca(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.mesAno(mesAno)
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_LIDERANCA, CAMPO_LIDERANCA);
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/gastos-mensais/{mesAno}/deputados/{deputado}"})
	public Resultado<Cota> tipoDespesaGastosMensaisDeputadoNomeDeputado(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("mesAno") String mesAno, 
			@PathVariable("deputado") String deputado, 
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorDeputado(true)
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.mesAno(mesAno)
				.quantidade(filtro.getQuantidade())
				.build(); 
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	@GetMapping(value = { despesaUrl+"/{tipoDespesa}/gastos-mensais/{mesAno}/liderancas/{lideranca}"})
	public Resultado<Cota> tipoDespesaGastosMensaisLiderancaNomeLideranca(@PathVariable("tipoDespesa") String tipoDespesa, 
			@PathVariable("mesAno") String mesAno, 
			@PathVariable("lideranca") String lideranca, 
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.tipoDespesa(tipoDespesa)
				.pesquisaPorLideranca(true)
				.mesAno(mesAno)
				.lideranca(Arrays.asList(lideranca))
				.quantidade(filtro.getQuantidade())
				.build(); 
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	// DEPUTADOS :: GASTOS MENSAIS 
	/***
	 * http://localhost:8082/api/cotas/deputado/gastos-mensais?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/deputado/Elmar%20Nascimento/gastos-mensais/8-2020
	 * http://localhost:8082/api/cotas/deputado/Elmar%20Nascimento/gastos-mensais/8-2020/TELEFONIA
	 * **/
	
	@GetMapping(value = { deputadoUrl+"/gastos-mensais" })
	public Map<String, Object> deputadoXGastosMensaisDe2Niveis(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.deputadoPartidoUfId(filtro.getDeputadoPartidoUfId())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_DEPUTADO, CAMPO_DEPUTADO, ROTULO_MES_ANO, CAMPO_MES_ANO, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { deputadoUrl+"/{deputado}/gastos-mensais/{mesAno}"})
	public Map<String, Object> deputadoXGastosMensaisDe1Nivel(@PathVariable("deputado") String deputado, 
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.mesAno(mesAno)
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_TIPO_DESPESA, CAMPO_TIPO_DESPESA);
	}
	
	@GetMapping(value = { deputadoUrl+"/{deputado}/gastos-mensais/{mesAno}/{tipoDespesa}"})
	public Resultado<Cota> deputadoXGastosMensaisDe0Nivel(@PathVariable("deputado") String deputado, 
			@PathVariable("mesAno") String mesAno,
			@PathVariable("tipoDespesa") String tipoDespesa,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.tipoDespesa(tipoDespesa)
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	// LIDERANCAS :: GASTOS MENSAIS
	/**
	 * http://localhost:8082/api/cotas/lideranca/gastos-mensais?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/lideranca/LIDERAN%C3%87A%20DO%20PT/gastos-mensais/8-2020?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/lideranca/LIDERAN%C3%87A%20DO%20PT/gastos-mensais/8-2020/LOCA%C3%87%C3%83O%20OU%20FRETAMENTO%20DE%20VE%C3%8DCULOS%20AUTOMOTORES
	 * **/
		
	@GetMapping(value = { liderancaUrl+"/gastos-mensais" })
	public Map<String, Object> liderancaXGastosMensaisDe2Niveis(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.lideranca(filtro.getLiderancas())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_LIDERANCA, CAMPO_LIDERANCA, ROTULO_MES_ANO, CAMPO_MES_ANO, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { liderancaUrl+"/{lideranca}/gastos-mensais/{mesAno}"})
	public Map<String, Object> liderancaXGastosMensaisDe1Nivel(@PathVariable("lideranca") String lideranca, 
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.lideranca(Arrays.asList(lideranca))
				.mesAno(mesAno)
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_TIPO_DESPESA, CAMPO_TIPO_DESPESA);
	}
	
	@GetMapping(value = { liderancaUrl+"/{lideranca}/gastos-mensais/{mesAno}/{tipoDespesa}"})
	public Resultado<Cota> liderancaXGastosMensaisDe0Nivel(@PathVariable("lideranca") String lideranca, 
			@PathVariable("mesAno") String mesAno,
			@PathVariable("tipoDespesa") String tipoDespesa,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.lideranca(Arrays.asList(lideranca))
				.tipoDespesa(tipoDespesa)
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	
	// FORNECEDOR :: GASTOS MENSAIS
	/**
	 * http://localhost:8082/api/cotas/fornecedor/gastos-mensais/
	 * 
	 * http://localhost:8082/api/cotas/fornecedor/SBH%20INFORMATICA%20LTDA_08898208000181/gastos-mensais/8-2020/liderancas?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/fornecedor/PANTANAL%20VE%C3%8DCULOS%20LTDA_07319323000191/gastos-mensais/8-2020/deputados?campoOrdenacao=valor
	 * 
	 * http://localhost:8082/api/cotas/fornecedor/PANTANAL%20VE%C3%8DCULOS%20LTDA_07319323000191/gastos-mensais/8-2020/deputados/Alex%20Santana
	 * http://localhost:8082/api/cotas/fornecedor/SBH%20INFORMATICA%20LTDA_08898208000181/gastos-mensais/8-2020/liderancas/PROS
	 * **/
	
	@GetMapping(value = { fornecedorUrl })
	public Map<String, Object> fornecedor(
			@RequestParam(name="mesAnoInicial", required = false) String mesAnoInicial,
			@RequestParam(name="mesAnoFinal", required = false) String mesAnoFinal,
			@RequestParam(name="geral", required = false) String geral
			) {
		ParametrosCota param = ParametrosCota.builder()
				.mesAnoFinal(mesAnoFinal)
				.mesAnoInicial(mesAnoInicial)
				.geral(geral)
				.build();
		Map<String, Object> resultado = servicoCotas.agregacao(param, ROTULO_FORNECEDOR, CAMPO_FORNECEDOR);
		return resultado;
	}
	
	@GetMapping(value = { fornecedorUrl+"/gastos-mensais" })
	public Map<String, Object> fornecedorXGastosMensaisDe2Niveis(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.nomeFornecedorCpfCnpj(filtro.getNomeFornecedorCpfCnpj())
				.cnpjCpfFornecedor(filtro.getCnpjCpfFornecedor())
				.numeroDocumento(filtro.getNumeroDocumento())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_FORNECEDOR, CAMPO_FORNECEDOR, ROTULO_MES_ANO, CAMPO_MES_ANO, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/gastos-mensais/{mesAno}/deputados"})
	public Map<String, Object> fornecedorXGastosMensaisDe1NivelDeputado(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.mesAno(mesAno)
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_DEPUTADO, CAMPO_DEPUTADO);
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/gastos-mensais/{mesAno}/liderancas"})
	public Map<String, Object> fornecedorXGastosMensaisDe1NivelLideranca(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.mesAno(mesAno)
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, "lideranca", "lideranca");
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/gastos-mensais/{mesAno}/deputados/{deputado}"})
	public Resultado<Cota> fornecedorXGastosMensaisDe0NivelDeputados(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("mesAno") String mesAno,
			@PathVariable("deputado") String deputado,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidade(filtro.getQuantidade())
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/gastos-mensais/{mesAno}/liderancas/{lideranca}"})
	public Resultado<Cota> fornecedorXgastosMensaisDe0NivelLideranca(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("lideranca") String lideranca,
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidade(filtro.getQuantidade())
				.lideranca(Arrays.asList(lideranca))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
		
	// FORNECEDOR :: LIDERANCA
	/**
	 * http://localhost:8082/api/cotas/fornecedor/liderancas/?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/fornecedor/SBH%20INFORMATICA%20LTDA_08898208000181/liderancas/pros/?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/fornecedor/SBH%20INFORMATICA%20LTDA_08898208000181/liderancas/pros/8-2020/
	 * **/
	
	@GetMapping(value = { fornecedorUrl+"/liderancas" })
	public Map<String, Object> fornecedorXLiderancaDe2Niveis(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.nomeFornecedorCpfCnpj(filtro.getNomeFornecedorCpfCnpj())
				.cnpjCpfFornecedor(filtro.getCnpjCpfFornecedor())
				.numeroDocumento(filtro.getNumeroDocumento())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_FORNECEDOR, CAMPO_FORNECEDOR, ROTULO_LIDERANCA, CAMPO_LIDERANCA, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/liderancas/{lideranca}"})
	public Map<String, Object> fornecedorXLiderancaDe1Nivel(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("lideranca") String lideranca,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.lideranca(Arrays.asList(lideranca))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_MES_ANO, CAMPO_MES_ANO);
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/liderancas/{lideranca}/{mesAno}"})
	public Resultado<Cota> fornecedorXLiderancaDe0Nivel(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("lideranca") String lideranca,
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorLideranca(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.lideranca(Arrays.asList(lideranca))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
	
	// FORNECEDOR :: DEPUTADO
	/**
	 * http://localhost:8082/api/cotas/fornecedor/gastos-mensais?campoOrdenacao=valor
	 * http://localhost:8082/api/cotas/fornecedor/EMBAIXADA%20GASTRONOMICA%20BAR_11999092000181/deputados/David%20Soares
	 * http://localhost:8082/api/cotas/fornecedor/EMBAIXADA%20GASTRONOMICA%20BAR_11999092000181/deputados/David%20Soares/8-2020
	 * **/
	
	@GetMapping(value = { fornecedorUrl+"/deputados" })
	public Map<String, Object> fornecedorXDeputadoDe2Niveis(ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota param = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.mesAnoFinal(filtro.getMesAnoFinal())
				.mesAnoInicial(filtro.getMesAnoInicial())
				.quantidadeNivel1(filtro.getQuantidade())
				.nomeFornecedorCpfCnpj(filtro.getNomeFornecedorCpfCnpj())
				.cnpjCpfFornecedor(filtro.getCnpjCpfFornecedor())
				.numeroDocumento(filtro.getNumeroDocumento())
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		Map<String, Object> resultado = servicoCotas.cotaDe2Niveis(param, ROTULO_FORNECEDOR, CAMPO_FORNECEDOR, ROTULO_DEPUTADO, CAMPO_DEPUTADO, ordenacao);
		return resultado;
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/deputados/{deputado}"})
	public Map<String, Object> fornecedorXDeputadoDe1Nivel(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("deputado") String deputado,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.build();
		Ordenacao ordenacao = formaDeOrdenarAgregacao(filtro.getCampoOrdenacao());
		return servicoCotas.cotaDe1Nivel(parametros, ordenacao, ROTULO_MES_ANO, CAMPO_MES_ANO);
	}
	
	@GetMapping(value = { fornecedorUrl+"/{fornecedor}/deputados/{deputado}/{mesAno}"})
	public Resultado<Cota> fornecedorXDeputadoDe0Nivel(@PathVariable("fornecedor") String fornecedor, 
			@PathVariable("deputado") String deputado,
			@PathVariable("mesAno") String mesAno,
			ParametroCotasSimplificadoDTO filtro) {
		ParametrosCota parametros = ParametrosCota.builder()
				.pesquisaPorDeputado(true)
				.quantidadeNivel1(filtro.getQuantidade())
				.deputadoPartidoUfId(Arrays.asList(deputado))
				.nomeFornecedorCpfCnpj(Arrays.asList(fornecedor))
				.mesAno(mesAno)
				.build();
		return servicoCotas.cotaDe0Nivel(parametros);
	}
		
	
	private Ordenacao formaDeOrdenarAgregacao(String campoOrdenacao) {
		Ordenacao ordenacaoPadrao = new Ordenacao(null, null);
		return campoOrdenacao != null ? campoOrdenacao.equals("valor") ? new Ordenacao("valor", TipoOrdenacao.DESC) : ordenacaoPadrao : ordenacaoPadrao;
	}

	private ParametrosCota converterDtoparametros(ParametroCotasDTO paramDto) {
		return ParametrosCota.builder()
				.deputado(paramDto.getDeputado())
				.lideranca(paramDto.getLideranca())
				.mesAno(paramDto.getMesAno())
				.mesAnoFinal(paramDto.getMesAnoFinal())
				.mesAnoInicial(paramDto.getMesAnoInicial())
				.ordenacao(paramDto.getOrdenacao())
				.pagina(paramDto.getPagina())
				.pesquisaPorDeputado(paramDto.isPesquisaPorDeputado())
				.pesquisaPorLideranca(paramDto.isPesquisaPorLideranca())
				.quantidade(paramDto.getQuantidade())
				.siglaPartido(paramDto.getSiglaPartido())
				.tipoDespesa(paramDto.getTipoDespesa())
				.uf(paramDto.getUf())
				.geral(paramDto.getGeral())
				.build();
	}
	
}