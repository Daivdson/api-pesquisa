package br.leg.camara.apipesquisa.aplicacao.api.cotas;

import java.util.Map;

import br.leg.camara.apipesquisa.aplicacao.dominio.Cota;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;

public interface RepositorioCotas {

	Resultado<Cota> listaGeral(ParametrosCota parametros);

	Map<String, Object> cotaDe2Niveis(ParametrosCota parametros,String rotuloPrimeiroNivel, String campoPrimeiroNivel, String rotuloSegundoNivel, String campoSegundoNivel, Ordenacao ordenacao);

	Map<String, Object> cotaDe1Nivel(ParametrosCota parametros, Ordenacao ordenacao, String rotuloPrimeiroNivel, String campoPrimeiroNivel);

	Resultado<Cota> cotaDe0Nivel(ParametrosCota parametros);

	Map<String, Object> agregacao(ParametrosCota parametros, String rotuloCampo, String campo);
	
}