package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplementation implements IMunicipioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetByIdEstado(int idEstado) {
        return jdbcTemplate.execute("{CALL MunicipiosGetByIdEstado(?,?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result result = new Result();

            try {

                callableStatement.setInt(1, idEstado);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    Municipio municipio = new Municipio();
                    municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                    municipio.setNombre(resultSet.getString("Nombre"));

                    result.objects.add(municipio);

                }

                result.correct = true;
            } catch (Exception ex) {
                result.correct = true;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
                result.objects = null;
            }

            return result;
        });
    }

}
