package br.leg.camara.apipesquisa.adaptadores.repositorio.rest.conversores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import br.leg.camara.apipesquisa.adaptadores.repositorio.rest.dto.EnqueteDTO;
import br.leg.camara.apipesquisa.aplicacao.dominio.Enquete;

public class ConversorEnquete implements Converter<EnqueteDTO, Enquete> {

	@Override
	public Enquete convert(EnqueteDTO source) {
		return new Enquete(source.getId(), source.getDescricaoEnquete(), source.getTituloEnquete(), source.getUrl());
	}

	public List<Enquete> convert(List<EnqueteDTO> sources) {
		return sources.stream().map(this::convert).collect(Collectors.toList());
	}

}
