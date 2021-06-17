package br.leg.camara.apipesquisa.aplicacao.enums;

import java.util.Calendar;
import java.util.Locale;

public enum Mes {
	
	JAN(1, "Janeiro"),
	FEV(2, "Fevereiro"),
	MAR(3, "Março"),
	ABR(4, "Abril"),
	MAI(5, "Maio"),
	JUN(6, "Junho"),
	JUL(7, "Julho"),
	AGO(8, "Agosto"),
	SET(9, "Setembro"),
	OUT(10, "Outubro"),
	NOV(11, "Novembro"),
	DEZ(12, "Dezembro");

	private static final Locale BRASIL = new Locale("pt", "BR");
	
	private int num;
	private String nome;

	Mes(int num, String nome) {
		this.num = num;
		this.nome = nome;
	}

	public String getSigla() {
		return this.name();
	}

	public String getNome() {
		return nome;
	}

	public int getNum() {
		return num;
	}

	public Mes anterior() {
		return this.num > 1 ? porNum(this.num - 1) : DEZ;
	}

	public String toString() {
		return this.nome;
	}
	
	public static Mes[] array(Mes de, Mes ate) {
		Mes[] array = new Mes[ate.num - de.num + 1];
		int i = 0;
		
		for(Mes mes : Mes.values()) {
			if(mes.num >= de.num && mes.num <= ate.num) {
				array[i++] = mes;
			}
		}
		
		return array;
	}
	
	public static Mes atual() {
		return Mes.values()[Calendar.getInstance(BRASIL).get(Calendar.MONTH)];
	}
	
	public static Mes porNum(int num) {
		for (Mes mes : Mes.values()) {
			if (mes.num == num) {
				return mes;
			}
		}
		return null;
	}

}
