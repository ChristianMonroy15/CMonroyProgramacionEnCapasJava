package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.PaisJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaisMapper {

    Pais mapToPais(PaisJPA paisJPA);
    PaisJPA mapToPaisJPA(Pais pais);

}
