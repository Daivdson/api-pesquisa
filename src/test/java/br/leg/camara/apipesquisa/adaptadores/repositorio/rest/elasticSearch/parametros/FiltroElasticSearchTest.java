package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.elasticSearch.parametros;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util.DataUtil;
import br.leg.camara.apipesquisa.aplicacao.dominio.RangeData;

@SpringBootTest
public class FiltroElasticSearchTest {
	
	
	@Test
	public void rangeDeData() {
		FiltroElasticSearch  filtro = new FiltroElasticSearch();
		
		Date dataInicio = DataUtil.conversorData("10-10-2019");
		Date dataFim = DataUtil.conversorData("10-12-2019");
		
		RangeData data = new RangeData(dataInicio, dataFim, "data");
		
		filtro.addRangeData(data);
		
		assertEquals("[{\"range\":{\"data\":{\"format\":\"dd-MM-YYYY\",\"gte\":\"10-10-2019\",\"lte\":\"10-12-2019\",\"time_zone\":\"America/Sao_Paulo\"}}}]", filtro.getFiltro().toString());
	}
	
	
	@Test
	public void filtroPadrao() {
		FiltroElasticSearch  filtro = new FiltroElasticSearch();
		
		filtro.add("colecao.keyword","Comissões");
		assertEquals("[{\"match_phrase\":{\"colecao.keyword\":\"Comissões\"}}]", filtro.getFiltro().toString());
		
		filtro.add("veiculo","agencia");
		assertEquals("[{\"match_phrase\":{\"colecao.keyword\":\"Comissões\"}}, {\"match_phrase\":{\"veiculo\":\"agencia\"}}]", filtro.getFiltro().toString());
	}
	
	@Test
	public void filtroPadraoEFiltroData() {
		FiltroElasticSearch  filtro = new FiltroElasticSearch();
		
		Date dataInicio = DataUtil.conversorData("10-10-2019");
		Date dataFim = DataUtil.conversorData("10-12-2019");
		RangeData data = new RangeData(dataInicio, dataFim, "data");
		
		// add parametros
		filtro.addRangeData(data);
		filtro.add("colecao.keyword","Comissões");
		filtro.add("veiculo","agencia");
		
		assertEquals("[{\"range\":{\"data\":{\"format\":\"dd-MM-YYYY\",\"gte\":\"10-10-2019\",\"lte\":\"10-12-2019\",\"time_zone\":\"America/Sao_Paulo\"}}}, "
				+ "{\"match_phrase\":{\"colecao.keyword\":\"Comissões\"}}, "
				+ "{\"match_phrase\":{\"veiculo\":\"agencia\"}}]", 
				filtro.getFiltro().toString());
	}
	

}
