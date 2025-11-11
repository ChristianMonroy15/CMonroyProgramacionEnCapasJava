
package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import java.util.List;


public interface IUsuarioDAO {
    
    Result GetAll();
    Result GetById(int detail);
    Result Add(Usuario usuario);
    Result Update(Usuario usuario);
    Result Delete(int idUsuario);
    Result GetAllDinamico(Usuario usuario);
    Result UpdateImagen(int idUsuario, String imagen);
    Result AddAll(List<Usuario> usuarios);
    
    
    
    
}
