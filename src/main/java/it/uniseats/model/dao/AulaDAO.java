package it.uniseats.model.dao;

import it.uniseats.model.beans.AulaBean;
import it.uniseats.model.beans.PostoBean;
import it.uniseats.model.beans.StudenteBean;
import it.uniseats.utils.DataSourceUtils;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AulaDAO {

    private static final String TABLE_NAME = "aula";
    private static final String DATASOURCE_ERROR = "[AULADAO] Errore: il DataSource non risulta essere configurato correttamente";

    public synchronized Object doQuery(String methodName, Object parameter) throws SQLException {

        DataSource ds = DataSourceUtils.getDataSource();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        if (ds != null) {

            String querySQL;

            try {
                connection = ds.getConnection();

                switch (methodName) {

                    case "doRetrieveByCode":
                        querySQL = "SELECT * FROM " + TABLE_NAME + " WHERE codice=?";
                        preparedStatement = connection.prepareStatement(querySQL);
                        return doRetrieveByCode(preparedStatement, (String) parameter);

                    case "doRetrieveAll":
                        querySQL = "SELECT * FROM " + TABLE_NAME;
                        preparedStatement = connection.prepareStatement(querySQL);
                        return doRetriveAll(preparedStatement);

                    default:
                        return null;

                }

            } finally {

                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } finally {
                    if (connection != null)
                        connection.close();
                }

            }

        } else {
            System.out.println(DATASOURCE_ERROR);
            return null;
        }

    }

    private static synchronized AulaBean doRetrieveByCode(PreparedStatement preparedStatement, String codice) throws SQLException {

        preparedStatement.setString(1, codice);
        ResultSet rs = preparedStatement.executeQuery();

        AulaBean aulaBean = new AulaBean();

        while (rs.next()) {
            aulaBean = getAulaInfo(rs);
        }

        return aulaBean;

    }

    private static synchronized ArrayList<AulaBean> doRetriveAll(PreparedStatement preparedStatement) throws SQLException {

        ArrayList<AulaBean> list = new ArrayList<>();
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            AulaBean aulaBean = getAulaInfo(rs);
            list.add(aulaBean);
        }

        return list;

    }

    private static AulaBean getAulaInfo(ResultSet rs) throws SQLException {

        AulaBean aulaBean = new AulaBean();

        aulaBean.setCodice(rs.getString("codice"));
        aulaBean.setDipartimento(rs.getString("dipartimento"));
        aulaBean.setnPosti(rs.getInt("numPosti"));
        aulaBean.setEdificio(rs.getString("edificio"));

        return aulaBean;
    }

}
