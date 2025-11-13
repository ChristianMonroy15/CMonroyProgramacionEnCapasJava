package com.digi01.CMonroyProgramacionNCapasSpring.Service;

import com.digi01.CMonroyProgramacionNCapasSpring.DAO.UsuarioJPADAOImplementation;
import com.digi01.CMonroyProgramacionNCapasSpring.JPA.UsuarioJPA;
import com.digi01.CMonroyProgramacionNCapasSpring.MAPPER.UsuarioMapper;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public List<Usuario> GetAll() {
        Result result = usuarioJPADAOImplementation.GetAll();
        return ((List<UsuarioJPA>) (List<?>) result.objects)
                .stream()
                .map(usuarioMapper::mapToUsuario)
                .toList();
    }

    @Transactional
    public boolean Add(Usuario usuario) {
        Result result = new Result();
        try {

            UsuarioJPA usuarioJPA = usuarioMapper.mapToUsuarioJPA(usuario);
            usuarioJPA.DireccionesJPA.get(0).UsuarioJPA = usuarioJPA;
            usuarioJPADAOImplementation.Add(usuarioJPA);

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result.correct;
    }

    public Usuario GetById(int IdUsuario) {

        Result result = usuarioJPADAOImplementation.GetById(IdUsuario);
        result.object = usuarioMapper.mapToUsuario((UsuarioJPA) result.object);
        return (Usuario) result.object;

    }

    @Transactional
    public Result Update(Usuario usuario) {

        Result result = new Result();

        try {
            Result resultUsuario = usuarioJPADAOImplementation.GetById(usuario.getIdUsuario());

            UsuarioJPA usuarioBD = (UsuarioJPA) resultUsuario.object;

            UsuarioJPA usuarioUpdate = usuarioMapper.mapToUsuarioJPA(usuario);

            usuarioUpdate.setPassword(usuarioBD.getPassword());

            usuarioJPADAOImplementation.Update(usuarioUpdate);

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;

    }
    
    @Transactional
    public Result Delete (int IdUsuario){
        
        Result result = usuarioJPADAOImplementation.Delete(IdUsuario);
        return result;
        
    }

}
