package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.UsuarioJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;

public interface IUsuarioJPA {

    Result GetAll();

    Result Add(UsuarioJPA usuarioJPA);

    Result GetById(int IdUsuario);

    Result Update(UsuarioJPA usuarioJPA);
    
    Result Delete(int IdUsuario);
    
    Result UpdateImagen(int IdUsuario, String Imagen);
    
    Result GetAllDinamico(Usuario usuario);

}
