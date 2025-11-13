package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.ColoniaJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MunicipioMapper.class})
public interface ColoniaMapper {

    @Mapping(source = "MunicipioJPA", target = "Municipio")
    Colonia mapToColonia(ColoniaJPA coloniaJPA);

    @Mapping(source = "Municipio", target = "MunicipioJPA")
    ColoniaJPA mapToColoniaJPA(Colonia colonia);

}
