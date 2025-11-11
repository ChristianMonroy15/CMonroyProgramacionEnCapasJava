package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Estado;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Rol;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    //Inyeccion de Dependencias
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetById(int detail) {
        Result result = jdbcTemplate.execute("{CALL UsuarioDireccionGetById(?,?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result resultSP = new Result();
            try {
                callableStatement.setInt(1, detail);
                callableStatement.registerOutParameter(2, Types.REF_CURSOR);
                callableStatement.registerOutParameter(3, Types.REF_CURSOR);

                callableStatement.execute();

                ResultSet resultSetUsuario = (ResultSet) callableStatement.getObject(2);
                if (resultSetUsuario.next()) {

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultSetUsuario.getInt("IdUsuario"));
                    usuario.setNombre(resultSetUsuario.getString("Nombre"));
                    usuario.setApellidoPaterno(resultSetUsuario.getString("ApellidoPaterno"));
                    usuario.setApellidoMaterno(resultSetUsuario.getString("ApellidoMaterno"));
                    usuario.setUsername(resultSetUsuario.getString("Username"));
                    usuario.setFechaNacimiento(resultSetUsuario.getDate("FechaNacimiento"));
                    usuario.setCurp(resultSetUsuario.getString("Curp"));
                    usuario.setEmail(resultSetUsuario.getString("Email"));
                    usuario.setCurp(resultSetUsuario.getString("Curp"));
                    usuario.setTelefono(resultSetUsuario.getString("Telefono"));
                    usuario.setCelular(resultSetUsuario.getString("Celular"));
                    usuario.setSexo(resultSetUsuario.getString("Sexo"));

                    usuario.Rol = new Rol();
                    usuario.Rol.setIdRol(resultSetUsuario.getInt("IdRol"));
                    usuario.Rol.setNombre(resultSetUsuario.getString("NombreRol"));

                    usuario.setImagen(resultSetUsuario.getString("Imagen"));

                    ResultSet resultSetDirecciones = (ResultSet) callableStatement.getObject(3);
                    usuario.Direcciones = new ArrayList<>();

                    while (resultSetDirecciones.next()) {

                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSetDirecciones.getInt("IdDireccion"));
                        direccion.setCalle(resultSetDirecciones.getString("Calle"));
                        direccion.setNumeroInterior(resultSetDirecciones.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSetDirecciones.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();

                        direccion.Colonia.setIdColonia(resultSetDirecciones.getInt("IdColonia"));
                        direccion.Colonia.setNombre(resultSetDirecciones.getString("NombreColonia"));
                        direccion.Colonia.setCodigoPostal(resultSetDirecciones.getString("CodigoPostal"));

                        direccion.Colonia.Municipio = new Municipio();
                        direccion.Colonia.Municipio.setIdMunicipio(resultSetDirecciones.getInt("IdMunicipio"));
                        direccion.Colonia.Municipio.setNombre(resultSetDirecciones.getString("NombreMunicipio"));

                        direccion.Colonia.Municipio.Estado = new Estado();
                        direccion.Colonia.Municipio.Estado.setIdEstado(resultSetDirecciones.getInt("IdEstado"));
                        direccion.Colonia.Municipio.Estado.setNombre(resultSetDirecciones.getString("NombreEstado"));

                        direccion.Colonia.Municipio.Estado.Pais = new Pais();
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSetDirecciones.getInt("IdPais"));
                        direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSetDirecciones.getString("NombrePais"));

                        usuario.Direcciones.add(direccion);

                    }
                    resultSP.object = usuario;
                    resultSP.correct = true;

                }

            } catch (Exception ex) {
                resultSP.correct = false;
                resultSP.errorMessage = ex.getLocalizedMessage();
                resultSP.ex = ex;
            }

            return resultSP;
        });
        return result;
    }

    @Override
    public Result GetAll() {
        Result result = jdbcTemplate.execute("{CALL UsuarioDireccionGetAll(?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result resultSP = new Result();

            try {
                callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
                resultSP.objects = new ArrayList<>();

                int idUsuario = 0;

                while (resultSet.next()) {

                    idUsuario = resultSet.getInt("idUsuario");

                    if (!resultSP.objects.isEmpty() && idUsuario == ((Usuario) (resultSP.objects.get(resultSP.objects.size() - 1))).getIdUsuario()) {
                        Direccion direccion = new Direccion();

                        direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();

                        direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                        direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                        direccion.Colonia.Municipio = new Municipio();
                        direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                        direccion.Colonia.Municipio.Estado = new Estado();
                        direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                        direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                        direccion.Colonia.Municipio.Estado.Pais = new Pais();
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                        direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                        Usuario usuario = ((Usuario) (resultSP.objects.get(resultSP.objects.size() - 1)));
                        usuario.Direcciones.add(direccion);

                    } else {

                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(resultSet.getInt("IdUsuario"));
                        usuario.setNombre(resultSet.getString("Nombre"));
                        usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                        usuario.setUsername(resultSet.getString("Username"));
                        usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuario.setCurp(resultSet.getString("Curp"));
                        usuario.setEmail(resultSet.getString("Email"));
                        usuario.setImagen(resultSet.getString("Imagen"));
                        usuario.setCurp(resultSet.getString("Curp"));
                        usuario.setTelefono(resultSet.getString("Telefono"));
                        usuario.setCelular(resultSet.getString("Celular"));
                        usuario.setSexo(resultSet.getString("Sexo"));

                        usuario.Rol = new Rol();
                        usuario.Rol.setIdRol(resultSet.getInt("IdRol"));
                        usuario.Rol.setNombre(resultSet.getString("NombreRol"));

                        usuario.Direcciones = new ArrayList<>();

                        Direccion direccion = new Direccion();

                        direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();

                        direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                        direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                        direccion.Colonia.Municipio = new Municipio();
                        direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                        direccion.Colonia.Municipio.Estado = new Estado();
                        direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                        direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                        direccion.Colonia.Municipio.Estado.Pais = new Pais();
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                        direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                        usuario.Direcciones.add(direccion);
                        resultSP.objects.add(usuario);
                    }

                }

                resultSP.correct = true;

            } catch (Exception ex) {
                resultSP.correct = false;
                resultSP.errorMessage = ex.getLocalizedMessage();
                resultSP.ex = ex;
            }
            return resultSP;
        });
        return result;
    }

    @Override
    public Result Add(Usuario usuario) {
        Result result = new Result();
        result.correct = jdbcTemplate.execute("{CALL UsuarioDireccionAdd(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Boolean>) (var callableStatement) -> {

            try {
                callableStatement.setString(1, usuario.getUsername());
                callableStatement.setString(2, usuario.getNombre());
                callableStatement.setString(3, usuario.getApellidoPaterno());
                callableStatement.setString(4, usuario.getApellidoMaterno());
                callableStatement.setString(5, usuario.getEmail());
                callableStatement.setString(6, usuario.getPassword());

                callableStatement.setDate(7, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                callableStatement.setString(8, usuario.getSexo());
                callableStatement.setString(9, usuario.getTelefono());
                callableStatement.setString(10, usuario.getCelular());
                callableStatement.setString(11, usuario.getCurp());
                callableStatement.setInt(12, usuario.Rol.getIdRol());
                callableStatement.setString(13, usuario.getImagen());

                callableStatement.setString(14, usuario.Direcciones.get(0).getCalle());
                callableStatement.setString(15, usuario.Direcciones.get(0).getNumeroInterior());
                callableStatement.setString(16, usuario.Direcciones.get(0).getNumeroExterior());
                callableStatement.setInt(17, usuario.Direcciones.get(0).Colonia.getIdColonia());

                int rowAffected = callableStatement.executeUpdate();
                if (rowAffected > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se registro:( ";

                }
                result.correct = true;

            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }
            return true;
        });

        return result;
    }

    @Override
    public Result Update(Usuario usuario) {

        return jdbcTemplate.execute("{CALL UsuarioUpdate(?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();
            try {

                callableStatement.setInt(1, usuario.getIdUsuario());
                callableStatement.setString(2, usuario.getUsername());
                callableStatement.setString(3, usuario.getNombre());
                callableStatement.setString(4, usuario.getApellidoPaterno());
                callableStatement.setString(5, usuario.getApellidoMaterno());
                callableStatement.setString(6, usuario.getEmail());

                callableStatement.setDate(7, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                callableStatement.setString(8, usuario.getSexo());
                callableStatement.setString(9, usuario.getTelefono());
                callableStatement.setString(10, usuario.getCelular());
                callableStatement.setString(11, usuario.getCurp());
                callableStatement.setInt(12, usuario.Rol.getIdRol());

                int rowAffected = callableStatement.executeUpdate();
                if (rowAffected > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se Actualizo:( ";

                }
                result.correct = true;
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }

            return result;
        });
    }

    @Override
    public Result Delete(int idUsuario) {
        return jdbcTemplate.execute("{CALL UsuarioDireccionDelete(?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {
                callableStatement.setInt(1, idUsuario);

                int arrowAfeccted = callableStatement.executeUpdate();
                if (arrowAfeccted > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se elimino:( ";
                }
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }

            return result;
        });
    }

    @Override
    public Result GetAllDinamico(Usuario usuario) {
        return jdbcTemplate.execute("{CALL UsuarioGetAllDinamico (?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {

                callableStatement.setString(1, usuario.getNombre());
                callableStatement.setString(2, usuario.getApellidoPaterno());
                callableStatement.setString(3, usuario.getApellidoMaterno());
                callableStatement.setInt(4, usuario.Rol.getIdRol());
                callableStatement.registerOutParameter(5, Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(5);
                result.objects = new ArrayList<>();

                int idUsuario = 0;

                while (resultSet.next()) {

                    idUsuario = resultSet.getInt("idUsuario");

                    if (!result.objects.isEmpty() && idUsuario == ((Usuario) (result.objects.get(result.objects.size() - 1))).getIdUsuario()) {
                        Direccion direccion = new Direccion();

                        direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();

                        direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                        direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                        direccion.Colonia.Municipio = new Municipio();
                        direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                        direccion.Colonia.Municipio.Estado = new Estado();
                        direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                        direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                        direccion.Colonia.Municipio.Estado.Pais = new Pais();
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                        direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                        Usuario usuario2 = ((Usuario) (result.objects.get(result.objects.size() - 1)));
                        usuario2.Direcciones.add(direccion);

                    } else {

                        Usuario usuario2 = new Usuario();
                        usuario2.setIdUsuario(resultSet.getInt("IdUsuario"));
                        usuario2.setNombre(resultSet.getString("Nombre"));
                        usuario2.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuario2.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                        usuario2.setUsername(resultSet.getString("Username"));
                        usuario2.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuario2.setCurp(resultSet.getString("Curp"));
                        usuario2.setEmail(resultSet.getString("Email"));
                        usuario2.setImagen(resultSet.getString("Imagen"));
                        usuario2.setCurp(resultSet.getString("Curp"));
                        usuario2.setTelefono(resultSet.getString("Telefono"));
                        usuario2.setCelular(resultSet.getString("Celular"));
                        usuario2.setSexo(resultSet.getString("Sexo"));

                        usuario2.Rol = new Rol();
                        usuario2.Rol.setIdRol(resultSet.getInt("IdRol"));
                        usuario2.Rol.setNombre(resultSet.getString("NombreRol"));

                        usuario2.Direcciones = new ArrayList<>();

                        Direccion direccion = new Direccion();

                        direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();

                        direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                        direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                        direccion.Colonia.Municipio = new Municipio();
                        direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                        direccion.Colonia.Municipio.Estado = new Estado();
                        direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                        direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                        direccion.Colonia.Municipio.Estado.Pais = new Pais();
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                        direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                        usuario2.Direcciones.add(direccion);
                        result.objects.add(usuario2);
                    }

                }

                result.correct = true;

            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }
            return result;
        });
    }

    @Override
    public Result UpdateImagen(int idUsuario, String imagen) {
        return jdbcTemplate.execute("{CALL UpdateImagen(?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {

                callableStatement.setInt(1, idUsuario);
                callableStatement.setString(2, imagen);
                callableStatement.executeUpdate();

                int rowAffected = callableStatement.executeUpdate();
                if (rowAffected > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se Actualizo:( ";

                }
                result.correct = true;

            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }

            return result;
        });
    }

    @Transactional
    @Override
    public Result AddAll(List<Usuario> usuarios) {
        Result result = new Result();

        try {
            jdbcTemplate.batchUpdate("{CALL UsuarioAdd (?,?,?,?,?,?,?,?,?,?,?,?) }", usuarios,
                    usuarios.size(),
                    (callableStatement, usuario) -> {

                        callableStatement.setString(1, usuario.getUsername());
                        callableStatement.setString(2, usuario.getNombre());
                        callableStatement.setString(3, usuario.getApellidoPaterno());
                        callableStatement.setString(4, usuario.getApellidoMaterno());
                        callableStatement.setString(5, usuario.getEmail());
                        callableStatement.setString(6, usuario.getPassword());
                        callableStatement.setDate(7, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                        callableStatement.setString(8, usuario.getSexo());
                        callableStatement.setString(9, usuario.getTelefono());
                        callableStatement.setString(10, usuario.getCelular());
                        callableStatement.setString(11, usuario.getCurp());
                        callableStatement.setInt(12, usuario.Rol.getIdRol());

                    });

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}
