package br.leg.camara.apipesquisa.adaptadores.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.leg.camara.apipesquisa.adaptadores.controlador.dto.LicitacaoDto;
import br.leg.camara.apipesquisa.adaptadores.controlador.dto.ResultadoDto;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.ParametrosLicitacao;
import br.leg.camara.apipesquisa.aplicacao.api.licitacao.RepositorioLicitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Licitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.LicitacaoAgregacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

@RestController
@RequestMapping(value = "/api/licitacoes", produces = {"application/json", "application/xml"})
public class LicitacaoCtrlAPI {
	
	private RepositorioLicitacao servico;
	
	public LicitacaoCtrlAPI(RepositorioLicitacao servico) {
		this.servico = servico;
	}

	@GetMapping(value = { "", "/" }, produces = {"application/json"})
	public ResultadoDto<LicitacaoDto> licitacoes(ParametrosLicitacao parametros) { 
		
		Resultado<Licitacao> resultado = servico.listaGeral(parametros);
		List<LicitacaoDto> licitacao = converterParaDTO(resultado.getResultado());
		LicitacaoAgregacao agregacao = servico.agregacao(parametros);
		
		// Add agregação ao resultado
		Map<String, Object> mapAgregacao = new HashMap<>();
		mapAgregacao.put("ano", agregacao.getAno());
		mapAgregacao.put("faixaValor", agregacao.getFaixaValor());
		mapAgregacao.put("modalidade", agregacao.getModalidade());
		mapAgregacao.put("situacao", agregacao.getSituacaoSimplificada());
		mapAgregacao.put("anoProcesso", agregacao.getAnoProcesso());
				
		return new ResultadoDto<>(resultado.getQuantidadeTotal(), licitacao, mapAgregacao);
	}
	
	@GetMapping(value = {"/{id}" }, produces = {"application/json"})
	public @ResponseBody ResponseEntity<?> licitacao(
			@PathVariable(name = "id") int id) {
		Licitacao resultado = servico.licitacao(id);
		if (resultado != null)
			return new ResponseEntity<>(resultado, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	List<LicitacaoDto> converterParaDTO(List<Licitacao> licitacao) {
		if (licitacao == null) {
			return null;
		}
		return licitacao.stream()
			.map(v -> LicitacaoDto.builder()
					.id(v.getId())
					.dataPublicacao(v.getDataPublicacao())
					.dataAbertura(v.getDataAbertura())
					.dataJulgamento(v.getDataJulgamento())
					.ano(v.getAno())
					.modalidade(v.getModalidade())
					.numero(v.getNumero())
					.objeto(v.getObjeto())
					.situacao(v.getSituacao())
					.situacaoSimplificada(v.getSituacaoSimplificada())
					.valorTotal(v.getValorTotal())
					.anoProcesso(v.getAnoProcesso())
					.numeroProcesso(v.getNumeroProcesso())
					.compraDireta(v.getCompraDireta())
					.valorTotalAdjudicados(v.getValorTotalAdjudicados())
					.build())
			.collect(Collectors.toList());
	}

}
