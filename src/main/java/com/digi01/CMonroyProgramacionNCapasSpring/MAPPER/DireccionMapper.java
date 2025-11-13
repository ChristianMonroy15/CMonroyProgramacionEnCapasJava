package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.DireccionJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColoniaMapper.class})
public interface DireccionMapper {

    @Mapping(source = "ColoniaJPA", target = "Colonia")
    Direccion mapToDireccion(DireccionJPA direccionJPA);

    @Mapping(source = "Colonia", target = "ColoniaJPA")
    DireccionJPA mapToDireccionJPA(Direccion direccion);

}
