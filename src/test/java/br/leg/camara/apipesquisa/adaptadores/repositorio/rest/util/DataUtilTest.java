package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
	
	@Test
	public void depois() {
		LocalDate antes = LocalDate.of(2020, 5, 10);
		LocalDate hoje = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);		
		
		assertTrue(DataUtil.isDepois(paraDate(hoje), paraDate(sabado)));
		assertFalse(DataUtil.isDepois(paraDate(hoje), paraDate(antes)));
	}
	
	@Test
	public void antes() {
		LocalDate antes = LocalDate.of(2020, 5, 10);
		LocalDate hoje = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);		
		
		assertTrue(DataUtil.isAntes(paraDate(hoje), paraDate(antes)));
		assertFalse(DataUtil.isAntes(paraDate(hoje), paraDate(sabado)));
	}
	
	@Test
	public void mesmo_dia() {
		LocalDate sexta1 = LocalDate.of(2020, 5, 15);
		LocalDate sexta2 = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);		
		
		assertTrue(DataUtil.isMesmoDia(paraDate(sexta1), paraDate(sexta2)));
		assertFalse(DataUtil.isMesmoDia(paraDate(sexta1), paraDate(sabado)));
	}
	
	@Test
	public void dia_seguinte() {
		LocalDate sexta = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);
		LocalDate domingo = LocalDate.of(2020, 5, 17);
		
		assertTrue(DataUtil.isDiaSeguinte(paraDate(sexta), paraDate(sabado)));
		assertFalse(DataUtil.isDiaSeguinte(paraDate(sexta), paraDate(domingo)));
	}
	
	@Test
	public void dia_anterior() {
		LocalDate sexta = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);
		LocalDate domingo = LocalDate.of(2020, 5, 17);
		
		assertTrue(DataUtil.isDiaAnterior(paraDate(sabado), paraDate(sexta)));
		assertFalse(DataUtil.isDiaAnterior(paraDate(domingo), paraDate(sexta)));
	}
	
	@Test
	public void proximo_dia_util() {
		LocalDate sexta = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);
		LocalDate domingo = LocalDate.of(2020, 5, 17);
		LocalDate segunda = LocalDate.of(2020, 5, 18);
		LocalDate terca = LocalDate.of(2020, 5, 19);
		
		assertEquals(paraDate(segunda), DataUtil.proximoDiaUtil(sexta));
		assertEquals(paraDate(segunda), DataUtil.proximoDiaUtil(sabado));
		assertEquals(paraDate(segunda), DataUtil.proximoDiaUtil(domingo));
		assertEquals(paraDate(terca), DataUtil.proximoDiaUtil(segunda));
	}
	
	@Test
	public void ultimo_dia_util() {
		LocalDate quinta = LocalDate.of(2020, 5, 14);
		LocalDate sexta = LocalDate.of(2020, 5, 15);
		LocalDate sabado = LocalDate.of(2020, 5, 16);
		LocalDate domingo = LocalDate.of(2020, 5, 17);
		LocalDate segunda = LocalDate.of(2020, 5, 18);		
		
		assertEquals(paraDate(quinta), DataUtil.ultimoDiaUtil(sexta));
		assertEquals(paraDate(sexta), DataUtil.ultimoDiaUtil(sabado));
		assertEquals(paraDate(sexta), DataUtil.ultimoDiaUtil(domingo));
		assertEquals(paraDate(sexta), DataUtil.ultimoDiaUtil(segunda));
	}
		
	private Date paraDate(LocalDate localDate) {		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
