package com.digi01.CMonroyProgramacionNCapasSpring.DAO;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaisDAOImplementation implements IPaisDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetAll() {

        Result result = jdbcTemplate.execute("{CALL PaisSP(?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result resultSP = new Result();

            try {

                callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
                resultSP.objects = new ArrayList<>();

                while (resultSet.next()) {

                    Pais pais = new Pais();

                    pais.setIdPais(resultSet.getInt("IdPais"));
                    pais.setNombre(resultSet.getString("Nombre"));

                    resultSP.objects.add(pais);
                }
                resultSP.correct = true;
            } catch (Exception ex) {
                resultSP.correct = true;
                resultSP.errorMessage = ex.getLocalizedMessage();
                resultSP.ex = ex;
            }

            return resultSP;
        });

        return result;
    }

}
