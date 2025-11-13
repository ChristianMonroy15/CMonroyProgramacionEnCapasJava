package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.JPA.DireccionJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.JPA.UsuarioJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionJPADAOImplementation implements IDireccionJPA {

    @Autowired
    private EntityManager entityManager; //DataSource

    @Override
    public Result Add(DireccionJPA direccionJPA, int IdUsuario) {
        Result result = new Result();

        try {

            UsuarioJPA usuario = entityManager.find(UsuarioJPA.class, IdUsuario);
            direccionJPA.setUsuarioJPA(usuario);

            entityManager.persist(direccionJPA);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result Update(DireccionJPA direccionJPA, int IdUsuario) {
        Result result = new Result();

        try {
            
            UsuarioJPA usuario = entityManager.find(UsuarioJPA.class, IdUsuario);
            direccionJPA.setUsuarioJPA(usuario);
      
            entityManager.merge(direccionJPA);
            result.correct = true;

        } catch (Exception ex) {

            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;

        }

        return result;
    }

}
