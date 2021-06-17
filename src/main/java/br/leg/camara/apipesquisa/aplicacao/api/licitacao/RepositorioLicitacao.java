package br.leg.camara.apipesquisa.aplicacao.api.licitacao;

import br.leg.camara.apipesquisa.aplicacao.dominio.Licitacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.LicitacaoAgregacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

public interface RepositorioLicitacao {

	Resultado<Licitacao> listaGeral(ParametrosLicitacao parametros);

	Licitacao licitacao(int id);

	LicitacaoAgregacao agregacao(ParametrosLicitacao parametros);

}
