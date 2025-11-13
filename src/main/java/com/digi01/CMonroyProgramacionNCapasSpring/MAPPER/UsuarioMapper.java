package com.digi01.CMonroyProgramacionNCapasSpring.MAPPER;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.UsuarioJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RolMapper.class, DireccionMapper.class})
public interface UsuarioMapper {

    @Mapping(source = "direccionesJPA", target = "direcciones")
    Usuario mapToUsuario(UsuarioJPA usuarioJPA);

    @Mapping(source = "direcciones", target = "direccionesJPA")
    UsuarioJPA mapToUsuarioJPA(Usuario usuario);

}
