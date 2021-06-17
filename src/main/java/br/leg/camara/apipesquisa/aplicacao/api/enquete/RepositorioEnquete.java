package br.leg.camara.apipesquisa.aplicacao.api.enquete;

import java.util.List;

import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;

public interface RepositorioEnquete {

	List<Enquete> maisVotadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

	List<Enquete> maisVotadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

	List<Enquete> maisVotadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

	List<Enquete> maisComentadas7Dias(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

	List<Enquete> maisComentadas1Mes(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

	List<Enquete> maisComentadas6Meses(Integer quantidade, String temaPortal, boolean validoPortal, boolean emTramitacao);

}
