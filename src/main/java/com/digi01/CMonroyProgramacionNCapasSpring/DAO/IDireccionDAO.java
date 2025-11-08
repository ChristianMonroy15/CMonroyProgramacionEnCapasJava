
package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;


public interface IDireccionDAO {
    Result Add(Direccion direccion, int id);
    
    Result GetById(int idDireccion);
    
    Result Update(Direccion direccion);
    
    Result Delete(int IdDirecion);
    
}
