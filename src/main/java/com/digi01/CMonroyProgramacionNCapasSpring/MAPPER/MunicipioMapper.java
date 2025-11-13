package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.MunicipioJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EstadoMapper.class})
public interface MunicipioMapper {

    @Mapping(source = "EstadoJPA", target = "Estado")
    Municipio mapToMunicipio(MunicipioJPA municipioJPA);

    @Mapping(source = "Estado", target = "EstadoJPA")
    MunicipioJPA mapToMunicipioJPA(Municipio municipio);

}
