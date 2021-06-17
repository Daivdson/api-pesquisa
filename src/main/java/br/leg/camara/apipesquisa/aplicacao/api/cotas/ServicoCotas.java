package br.leg.camara.apipesquisa.aplicacao.api.cotas;

import java.util.Map;

import br.leg.camara.apipesquisa.aplicacao.dominio.Cota;
import br.leg.camara.apipesquisa.aplicacao.dominio.Ordenacao;
import br.leg.camara.apipesquisa.aplicacao.dominio.Resultado;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServicoCotas {

	private final RepositorioCotas repositorioCotas;

	public Resultado<Cota> listaGeral(ParametrosCota parametros) {
		return this.repositorioCotas.listaGeral(parametros);
	}
	
	public Map<String, Object> cotaDe2Niveis(ParametrosCota parametros,String rotuloPrimeiroNivel, String campoPrimeiroNivel, String rotuloSegundoNivel, String campoSegundoNivel, Ordenacao ordenacao) {
		return this.repositorioCotas.cotaDe2Niveis(parametros, rotuloPrimeiroNivel, campoPrimeiroNivel, rotuloSegundoNivel, campoSegundoNivel, ordenacao);
	}
	
	public Map<String, Object> cotaDe1Nivel(ParametrosCota parametros, Ordenacao ordenacao, String rotuloPrimeiroNivel, String campoPrimeiroNivel) {
		return this.repositorioCotas.cotaDe1Nivel(parametros, ordenacao, rotuloPrimeiroNivel, campoPrimeiroNivel);
	}
	
	public Resultado<Cota> cotaDe0Nivel(ParametrosCota parametros) {
		return this.repositorioCotas.cotaDe0Nivel(parametros);
	}
	
	public Map<String, Object> agregacao(ParametrosCota parametros, String rotuloCampo, String campo){
		return this.repositorioCotas.agregacao(parametros, rotuloCampo, campo);
	}

}