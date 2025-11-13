package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.EstadoJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PaisMapper.class})
public interface EstadoMapper {

    @Mapping(source = "PaisJPA", target = "Pais")
    Estado mapToEstado(EstadoJPA estadoJPA);

    @Mapping(source = "Pais", target = "PaisJPA")
    EstadoJPA mapToEstadoJPA(Estado estado);

}
