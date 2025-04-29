// Dentro da sua classe SequenceManager.java
package cadastrobd.model.util; // Adicione a declaração do pacote

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceManager {

    public static Integer getValue(String sequenceName) {
        if (!sequenceName.equalsIgnoreCase("CodigoPessoa")) {
            System.out.println("Sequence não suportada: " + sequenceName);
            return -1; // Ou lance uma exceção
        }

        Connection conexao = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int nextId = -1;

        try {
            conexao = ConectorBD.getConnection();
            if (conexao == null) {
                System.out.println("Erro ao obter conexão com o banco de dados.");
                return -1;
            }

            // Consulta para obter o próximo valor e incrementá-lo atomicamente
            String sql = "UPDATE dbo.CodigoPessoa " +
                         "SET ProximoCodigo = ProximoCodigo + 1 " +
                         "OUTPUT inserted.ProximoCodigo;";

            preparedStatement = conexao.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nextId = resultSet.getInt("ProximoCodigo");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o próximo valor da sequência CodigoPessoa: " + e.getMessage());
        } finally {
            ConectorBD.close(resultSet);
            ConectorBD.close(preparedStatement);
            ConectorBD.close(conexao);
        }
        return nextId;
    }
}