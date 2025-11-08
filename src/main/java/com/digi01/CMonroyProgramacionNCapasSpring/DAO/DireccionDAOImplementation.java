package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Estado;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionDAOImplementation implements IDireccionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result Add(Direccion direccion, int id) {
        return jdbcTemplate.execute("{CALL DireccionAdd(?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();
            try {
                callableStatement.setString(1, direccion.getCalle());
                callableStatement.setString(2, direccion.getNumeroInterior());
                callableStatement.setString(3, direccion.getNumeroExterior());
                callableStatement.setInt(4, direccion.Colonia.getIdColonia());
                callableStatement.setInt(5, id);

                callableStatement.execute();

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
    public Result GetById(int idDireccion) {
        return jdbcTemplate.execute("{CALL DireccionGetById (?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();
            try {

                callableStatement.setInt(1, idDireccion);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
                if (resultSet.next()) {
                    Direccion direccion = new Direccion();
                    direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                    direccion.setCalle(resultSet.getString("Calle"));
                    direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));
                    direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));

                    direccion.Colonia = new com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia();

                    direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                    direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                    direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                    direccion.Colonia.Municipio = new com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio();

                    direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                    direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                    direccion.Colonia.Municipio.Estado = new com.digi01.CMonroyProgramacionNCapasSpring.ML.Estado();
                    direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                    direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                    direccion.Colonia.Municipio.Estado.Pais = new com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais();
                    direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                    direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                    result.object = direccion;
                    result.correct = true;

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
    public Result Update(Direccion direccion) {
        return jdbcTemplate.execute("{CALL DireccionUpdate(?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {
                callableStatement.setInt(1, direccion.getIdDireccion());
                callableStatement.setString(2, direccion.getCalle());
                callableStatement.setString(3, direccion.getNumeroInterior());
                callableStatement.setString(4, direccion.getNumeroExterior());
                callableStatement.setInt(5, direccion.Colonia.getIdColonia());

                int rowAffected = callableStatement.executeUpdate();
                if (rowAffected > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se Actualizo la direccion:( ";

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
    public Result Delete(int IdDirecion) {
        return jdbcTemplate.execute("{CALL DireccionDelete(?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {
                callableStatement.setInt(1, IdDirecion);
                callableStatement.execute();
                int rowAffected = callableStatement.executeUpdate();
                if (rowAffected > 0) {
                    result.correct = true;
                } else {
                    result.correct = false;
                    result.errorMessage = "No se Elimino:( ";

                }
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }

            return result;
        });
    }

}
