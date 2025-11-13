package com.digi01.CMonroyProgramacionNCapasSpring.Service;

import com.digi01.CMonroyProgramacionNCapasSpring.DAO.DireccionJPADAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.JPA.DireccionJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.MAPPER.DireccionMapper;
import com.digi01.CMonroyProgramacionNCapasSpring.MAPPER.UsuarioMapper;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DireccionService {

    @Autowired
    private DireccionJPADAOImplementation direccionJPADAOImplementation;

    @Autowired
    private DireccionMapper direccionMapper;

    @Transactional
    public boolean Add(Direccion direccion, int IdUsuario) {
        Result result = new Result();

        try {

            DireccionJPA direccionJPA = direccionMapper.mapToDireccionJPA(direccion);
            direccionJPADAOImplementation.Add(direccionJPA, IdUsuario);
            result.correct = true;

        } catch (Exception ex) {

            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;

        }

        return result.correct;
    }

    @Transactional
    public boolean Update(Direccion direccion, int IdUsuario) {
        Result result = new Result();

        try {
            DireccionJPA direccionJPA = direccionMapper.mapToDireccionJPA(direccion);
            direccionJPADAOImplementation.Update(direccionJPA, IdUsuario);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result.correct;
    }

    @Transactional
    public boolean Delete(int IdDireccion) {
        Result result = new Result();
        try {

            direccionJPADAOImplementation.Delete(IdDireccion);
            result.correct = true;

        } catch (Exception ex) {

            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;

        }

        return result.correct;
    }
    
    public Direccion GetById(int IdDireccion){
    
        Result result = direccionJPADAOImplementation.GetById(IdDireccion);
        result.object = direccionMapper.mapToDireccion((DireccionJPA) result.object);
        return (Direccion) result.object;
        
    }

}
