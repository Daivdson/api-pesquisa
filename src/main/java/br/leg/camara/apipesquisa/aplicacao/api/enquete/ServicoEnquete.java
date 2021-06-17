package br.leg.camara.apipesquisa.aplicacao.api.enquete;

import java.util.List;

import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;

public class ServicoEnquete {

	private RepositorioEnquete repositorioEnquete;

	public ServicoEnquete(RepositorioEnquete repositorioEnquete) {
		this.repositorioEnquete = repositorioEnquete;
	}
	
	public List<Enquete> maisVotadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisVotadas7Dias(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	public List<Enquete> maisVotadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisVotadas1Mes(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	public List<Enquete> maisVotadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisVotadas6Meses(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	public List<Enquete> maisComentadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisComentadas7Dias(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	public List<Enquete> maisComentadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisComentadas1Mes(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	public List<Enquete> maisComentadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao) {
		return repositorioEnquete.maisComentadas6Meses(quantidade, temaPortal, validoPortal, emTramitacao);
	}

	
	
}
