package br.leg.camara.apipesquisa.adaptadores.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.leg.camara.apipesquisa.aplicacao.api.enquete.ServicoEnquete;
import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;

@RestController
@RequestMapping(value = "/api/enquetes", produces = {"application/json", "application/xml"})
public class EnqueteCtrlAPI {
	
	private final ServicoEnquete servicoEnquete;
	
	public EnqueteCtrlAPI(ServicoEnquete servicoEnquete) {
		this.servicoEnquete = servicoEnquete;
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_VOTADAS_7_DIAS }, produces = {"application/json"})
	public List<Enquete> maisVotadas7Dias(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisVotadas7Dias(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_VOTADAS_1_MES}, produces = {"application/json"})
	public List<Enquete> maisVotadas1Mes(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisVotadas1Mes(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_VOTADAS_6_MESES }, produces = {"application/json"})
	public List<Enquete> maisVotadas6Meses(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisVotadas6Meses(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_COMENTADAS_7_DIAS }, produces = {"application/json"})
	public List<Enquete> maisComentadas7Dias(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisComentadas7Dias(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_COMENTADAS_1_MES }, produces = {"application/json"})
	public List<Enquete> maisComentadas1Mes(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisComentadas1Mes(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	@GetMapping(value = { URLs.ENQUETES_MAIS_COMENTADAS_6_MESES }, produces = {"application/json"})
	public List<Enquete> maisComentadas6Meses(
			@RequestParam(name="quantidade") Integer quantidade,
			@RequestParam(name = "temaPortal", required = false) String temaPortal,
			@RequestParam(name="validoPortal", required = false) boolean validoPortal,
			@RequestParam(name="emTramitacao", required = false) boolean emTramitacao) {
		return servicoEnquete.maisComentadas6Meses(quantidade, temaPortal, validoPortal, emTramitacao);
	}

}
