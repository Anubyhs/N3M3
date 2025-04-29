/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastrobd.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @Francinaldo
 */
public class ConectorBD {
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost\\\\SQLEXPRESS:1433;databaseName=loja;user=app_master;password=sua_senha;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "app_master";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER).newInstance();
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            return null;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement getPrepared(Connection conexao, String sql) {
        try {
            return conexao.prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao preparar o SQL: " + e.getMessage());
            return null;
        }
    }

    public static ResultSet getSelect(PreparedStatement consulta) {
        try {
            return consulta.executeQuery();
        } catch (SQLException e) {
            System.out.println("Erro ao executar a consulta: " + e.getMessage());
            return null;
        }
    }

    public static void close(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar o Statement: " + e.getMessage());
        }
    }

    public static void close(ResultSet resultado) {
        try {
            if (resultado != null) {
                resultado.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
        }
    }

    public static void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}