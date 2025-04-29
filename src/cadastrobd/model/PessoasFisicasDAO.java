/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastrobd.model;

/**
 *
 * @Francinaldo
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;

public class PessoasFisicasDAO {
    public PessoasFisicas getPessoa(int id) {
        try {
            Connection conexao = ConectorBD.getConnection();

            if (conexao == null) {
                return null;
            }

            String sql = "SELECT IDPessoa, NomePessoa, Telefone, Email, Logradouro, Cidade, Estado, CPF " +
                         "FROM dbo.Pessoas " +
                         "WHERE IDPessoa = ?";

            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id);

            ResultSet resultSet = ConectorBD.getSelect(prepared);

            if (resultSet != null && resultSet.next()) {
                PessoasFisicas pessoasFisicas = criaPessoasFisicas(resultSet);

                ConectorBD.close(resultSet);
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return pessoasFisicas;
            }

            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao obter a pessoa física pelo id: " + e.getMessage());
            return null;
        }
    }

    public List<PessoasFisicas> getPessoas() {
        try {
            Connection conexao = ConectorBD.getConnection();

            if (conexao == null) {
                return null;
            }

            String sql = "SELECT IDPessoa, NomePessoa, Telefone, Email, Logradouro, Cidade, Estado, CPF " +
                         "FROM dbo.Pessoas";

            PreparedStatement prepared = conexao.prepareStatement(sql);

            ResultSet resultSet = ConectorBD.getSelect(prepared);

            List<PessoasFisicas> pessoas = new ArrayList<>();

            while (resultSet != null && resultSet.next()) {
                PessoasFisicas pessoasFisicas = criaPessoasFisicas(resultSet);
                pessoas.add(pessoasFisicas);
            }

            ConectorBD.close(resultSet);
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);

            return pessoas;
        } catch (SQLException e) {
            System.out.println("Erro ao obter todas as pessoas físicas: " + e.getMessage());
            return null;
        }
    }

    public boolean incluir(PessoasFisicas pessoasFisicas) {
        Connection conexao = null;
        PreparedStatement preparedPessoa = null;

        try {
            Integer nextId = SequenceManager.getValue("CodigoPessoa");

            if (nextId == -1) {
                System.out.println("Falha ao obter o próximo ID para PessoasFisicas.");
                return false;
            }

            pessoasFisicas.setId(nextId);
            conexao = ConectorBD.getConnection();

            if (conexao == null) {
                return false;
            }

            String sqlPessoa = "INSERT INTO dbo.Pessoas (IDPessoa, NomePessoa, Telefone, Email, Logradouro, Cidade, Estado, CPF) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            preparedPessoa.setInt(1, pessoasFisicas.getId());
            preparedPessoa.setString(2, pessoasFisicas.getNome());
            preparedPessoa.setString(3, pessoasFisicas.getTelefone());
            preparedPessoa.setString(4, pessoasFisicas.getEmail());
            preparedPessoa.setString(5, pessoasFisicas.getRua());
            preparedPessoa.setString(6, pessoasFisicas.getCidade());
            preparedPessoa.setString(7, pessoasFisicas.getEstado());
            preparedPessoa.setString(8, pessoasFisicas.getCpf());

            if (preparedPessoa.executeUpdate() <= 0) {
                ConectorBD.close(preparedPessoa);
                ConectorBD.close(conexao);
                return false;
            }
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao incluir a pessoa física: " + e.getMessage());
            return false;
        } finally {
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
        }
    }

    public boolean alterar(PessoasFisicas pessoasFisicas) {
        Connection conexao = null;
        PreparedStatement preparedPessoa = null;

        try {
            conexao = ConectorBD.getConnection();

            if (conexao == null) {
                return false;
            }

            String sqlPessoa = "UPDATE dbo.Pessoas SET NomePessoa = ?, Telefone = ?, Email = ?, Logradouro = ?, Cidade = ?, Estado = ?, CPF = ? WHERE IDPessoa = ?";
            preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            preparedPessoa.setString(1, pessoasFisicas.getNome());
            preparedPessoa.setString(2, pessoasFisicas.getTelefone());
            preparedPessoa.setString(3, pessoasFisicas.getEmail());
            preparedPessoa.setString(4, pessoasFisicas.getRua());
            preparedPessoa.setString(5, pessoasFisicas.getCidade());
            preparedPessoa.setString(6, pessoasFisicas.getEstado());
            preparedPessoa.setString(7, pessoasFisicas.getCpf());
            preparedPessoa.setInt(8, pessoasFisicas.getId());

            if (preparedPessoa.executeUpdate() <= 0) {
                ConectorBD.close(preparedPessoa);
                ConectorBD.close(conexao);
                return false;
            }
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar a pessoa física: " + e.getMessage());
            return false;
        } finally {
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
        }
    }

    public boolean excluir(int id) {
        Connection conexao = null;
        PreparedStatement preparedPessoa = null;

        try {
            conexao = ConectorBD.getConnection();

            if (conexao == null) {
                return false;
            }

            String sqlPessoa = "DELETE FROM dbo.Pessoas WHERE IDPessoa = ?";
            preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            preparedPessoa.setInt(1, id);

            if (preparedPessoa.executeUpdate() <= 0) {
                ConectorBD.close(preparedPessoa);
                ConectorBD.close(conexao);
                return false;
            }
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir a pessoa física: " + e.getMessage());
            return false;
        } finally {
            ConectorBD.close(preparedPessoa);
            ConectorBD.close(conexao);
        }
    }

    private static PessoasFisicas criaPessoasFisicas(ResultSet resultSet) throws SQLException {
        PessoasFisicas pessoasFisicas = new PessoasFisicas();
        pessoasFisicas.setId(resultSet.getInt("IDPessoa"));
        pessoasFisicas.setNome(resultSet.getString("NomePessoa"));
        pessoasFisicas.setTelefone(resultSet.getString("Telefone"));
        pessoasFisicas.setEmail(resultSet.getString("Email"));
        pessoasFisicas.setRua(resultSet.getString("Logradouro"));
        pessoasFisicas.setCidade(resultSet.getString("Cidade"));
        pessoasFisicas.setEstado(resultSet.getString("Estado"));
        pessoasFisicas.setCpf(resultSet.getString("CPF"));
        return pessoasFisicas;
    }
}