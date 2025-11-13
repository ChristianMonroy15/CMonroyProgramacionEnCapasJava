package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.RolJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {  
    
    Rol mapToRol(RolJPA rolJPA);
    
    RolJPA mapToRolJPA(Rol rol);
}
