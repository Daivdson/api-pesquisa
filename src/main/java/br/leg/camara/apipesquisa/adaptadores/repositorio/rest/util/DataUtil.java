package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataUtil {
	
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	
	public static Date conversorData(String valorData) {
		if (Strings.isNullOrEmpty(valorData)) {
			return null;
		}

		Date data = null;
		try {
			DateFormat dtSaida = new SimpleDateFormat("dd-MM-yyyy");
			data = dtSaida.parse(valorData);
		} catch (ParseException e) {
			log.error("Erro ao tentar converter Data", e);			
		}
		return data;
	}
	
	public static Date conversorData(String valorData, String pattern) {
		if (Strings.isNullOrEmpty(valorData)) {
			return null;
		}

		Date data = null;
		try {
			DateFormat dtSaida = new SimpleDateFormat(pattern);
			data = dtSaida.parse(valorData);
		} catch (ParseException e) {
			log.error("Erro ao tentar converter Data", e);			
		}
		return data;
	}

	public static Date dataAtual() {
		return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static boolean isMesmoDia(Date diaAtual, Date diaAtualOutro) {
		LocalDate dia = diaAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate da = diaAtualOutro.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return da.compareTo(dia) == 0;
	}
	
	public static boolean isDiaSeguinte(Date diaAtual, Date diaSeguinte) {
		LocalDate dia = diaAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate ds = diaSeguinte.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return ds.compareTo(dia.plusDays(1)) == 0;
	}

	public static boolean isDiaAnterior(Date diaAtual, Date diaAnterior) {
		LocalDate dia = diaAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate da = diaAnterior.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return da.compareTo(dia.minusDays(1)) == 0;
	}
	
	public static boolean isAntes(Date diaAtual, Date diaAnterior) {		
		LocalDate dia = diaAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate da = diaAnterior.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return da.isBefore(dia);
	}
	
	public static boolean isDepois(Date diaAtual, Date diaPosterior) {
		LocalDate dia = diaAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate da = diaPosterior.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return da.isAfter(dia);
	}

	public static Date ultimoDiaUtil(LocalDate dia) {
		LocalDate anterior = dia.minusDays(1);
		if (anterior.getDayOfWeek().getValue() > 5) {
			anterior = anterior.minusDays(anterior.getDayOfWeek().getValue() - 5);
		}

		return Date.from(anterior.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static Date proximoDiaUtil(LocalDate dia) {
		LocalDate proximo = dia.plusDays(1);
		if (proximo.getDayOfWeek().getValue() > 5) {
			proximo = proximo.plusDays(8 - proximo.getDayOfWeek().getValue());
		}

		return Date.from(proximo.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	// Para formatar datas nesse formato: 2012-07-03T19:00:00-03:00
	public static String paraString(String data, String pattern) {
		ZonedDateTime d = ZonedDateTime.parse(data);
		return d.format(DateTimeFormatter.ofPattern(pattern)).toString();
	}

	// Para criar datas nesse formato: 2012-07-03T19:00:00-03:00
	public static Date paraData(String data) {
		ZonedDateTime d = ZonedDateTime.parse(data);
		return Date.from(d.toInstant());
	}

	public static String paraString(Date data) {
		return paraString(data, "dd-MM-yyyy");
	}
	
	public static String paraString(LocalDate data) {
		return data != null ? data.format(FORMATTER) : null;
	}

	public static String paraString(Date data, String pattern) {
		ZonedDateTime d = ZonedDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());
		return d.format(DateTimeFormatter.ofPattern(pattern)).toString();
	}
	
	public static void main(String[] args) {
		System.out.println(conversorData("10-03-2021"));
		System.out.println(paraData("2012-07-03T19:00:00-03:00"));
	}
}