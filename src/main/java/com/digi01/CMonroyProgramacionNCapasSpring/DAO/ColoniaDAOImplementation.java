package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ColoniaDAOImplementation implements IColoniaDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Result GetByIdMunicipio(int idMunicipio) {
        return jdbcTemplate.execute("{CALL ColoniasGetByMunicipio (?,?)}", (CallableStatementCallback<Result>) callableStaetment -> {
            Result result = new Result();

            try {

                callableStaetment.setInt(1, idMunicipio);
                callableStaetment.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStaetment.execute();

                ResultSet resultSet = (ResultSet) callableStaetment.getObject(2);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    Colonia colonia = new Colonia();
                    colonia.setIdColonia(resultSet.getInt("IdColonia"));
                    colonia.setNombre(resultSet.getString("Nombre"));
                    colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                    result.objects.add(colonia);

                }

                result.correct = true;

            } catch (Exception ex) {

                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
                result.objects = null;
            }

            return result;
        });
    }

}
