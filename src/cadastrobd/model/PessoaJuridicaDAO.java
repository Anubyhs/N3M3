package cadastrobd.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PessoaJuridicaDAO {
    private static final Logger LOGGER = Logger.getLogger(PessoaJuridicaDAO.class.getName());

    public PessoaJuridica getPessoa(int id) {
        String sql = "SELECT p.IDPessoa, p.NomePessoa, p.Telefone, p.Email, p.Logradouro, p.Cidade, p.Estado, pj.CNPJ " +
                "FROM dbo.Pessoas p INNER JOIN dbo.PessoasJuridicas pj ON p.IDPessoa = pj.IDPessoa WHERE p.IDPessoa = ?";
        try (Connection conexao = ConectorBD.getConnection();
             PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql)) {

            if (conexao == null) {
                return null;
            }

            prepared.setInt(1, id);
            try (ResultSet resultSet = ConectorBD.getSelect(prepared)) {
                if (resultSet.next()) {
                    return criaPessoaJuridica(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter a pessoa jurídica pelo id: " + id, e);
        }
        return null;
    }

    public List<PessoaJuridica> getPessoas() {
        String sql = "SELECT p.IDPessoa, p.NomePessoa, p.Telefone, p.Email, p.Logradouro, p.Cidade, p.Estado, pj.CNPJ " +
                "FROM dbo.Pessoas p INNER JOIN dbo.PessoasJuridicas pj ON p.IDPessoa = pj.IDPessoa";
        List<PessoaJuridica> pessoas = new ArrayList<>();
        try (Connection conexao = ConectorBD.getConnection();
             PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
             ResultSet resultSet = ConectorBD.getSelect(prepared)) {

            if (conexao == null) {
                return pessoas;
            }

            while (resultSet.next()) {
                pessoas.add(criaPessoaJuridica(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter todas as pessoas jurídicas", e);
        }
        return pessoas;
    }

    public boolean incluir(PessoaJuridica pessoaJuridica) {
        Integer nextId = SequenceManager.getValue("CodigoPessoa");
        if (nextId == -1) {
            return false;
        }
        pessoaJuridica.setId(nextId);

        Connection conexao = null;
        try {
            conexao = ConectorBD.getConnection();
            if (conexao == null) {
                return false;
            }
            
            conexao.setAutoCommit(false);
            
            if (!inserirPessoa(conexao, pessoaJuridica) || !inserirPessoaJuridica(conexao, pessoaJuridica)) {
                conexao.rollback();
                return false;
            }
            
            conexao.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao incluir a pessoa jurídica", e);
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erro ao realizar rollback", ex);
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao fechar conexão", e);
                }
            }
        }
    }

    private boolean inserirPessoa(Connection conexao, PessoaJuridica pessoaJuridica) throws SQLException {
        String sqlPessoa = "INSERT INTO dbo.Pessoas (IDPessoa, NomePessoa, Telefone, Email, Logradouro, Cidade, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa)) {
            preparedPessoa.setInt(1, pessoaJuridica.getId());
            preparedPessoa.setString(2, pessoaJuridica.getNome());
            preparedPessoa.setString(3, pessoaJuridica.getTelefone());
            preparedPessoa.setString(4, pessoaJuridica.getEmail());
            preparedPessoa.setString(5, pessoaJuridica.getRua());
            preparedPessoa.setString(6, pessoaJuridica.getCidade());
            preparedPessoa.setString(7, pessoaJuridica.getEstado());
            
            return preparedPessoa.executeUpdate() > 0;
        }
    }

    private boolean inserirPessoaJuridica(Connection conexao, PessoaJuridica pessoaJuridica) throws SQLException {
        String sqlPessoaJuridica = "INSERT INTO dbo.PessoasJuridicas (IDPessoa, CNPJ) VALUES (?, ?)";
        try (PreparedStatement preparedPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica)) {
            preparedPessoaJuridica.setInt(1, pessoaJuridica.getId());
            preparedPessoaJuridica.setString(2, pessoaJuridica.getCnpj());
            
            return preparedPessoaJuridica.executeUpdate() > 0;
        }
    }

    public boolean alterar(PessoaJuridica pessoaJuridica) {
        Connection conexao = null;
        try {
            conexao = ConectorBD.getConnection();
            if (conexao == null) {
                return false;
            }
            
            conexao.setAutoCommit(false);
            
            if (!alterarPessoa(conexao, pessoaJuridica) || !alterarPessoaJuridica(conexao, pessoaJuridica)) {
                conexao.rollback();
                return false;
            }
            
            conexao.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao alterar a pessoa jurídica", e);
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erro ao realizar rollback", ex);
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao fechar conexão", e);
                }
            }
        }
    }

    private boolean alterarPessoa(Connection conexao, PessoaJuridica pessoaJuridica) throws SQLException {
        String sqlPessoa = "UPDATE dbo.Pessoas SET NomePessoa = ?, Telefone = ?, Email = ?, Logradouro = ?, Cidade = ?, Estado = ? WHERE IDPessoa = ?";
        try (PreparedStatement preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa)) {
            preparedPessoa.setString(1, pessoaJuridica.getNome());
            preparedPessoa.setString(2, pessoaJuridica.getTelefone());
            preparedPessoa.setString(3, pessoaJuridica.getEmail());
            preparedPessoa.setString(4, pessoaJuridica.getRua());
            preparedPessoa.setString(5, pessoaJuridica.getCidade());
            preparedPessoa.setString(6, pessoaJuridica.getEstado());
            preparedPessoa.setInt(7, pessoaJuridica.getId());
            
            return preparedPessoa.executeUpdate() > 0;
        }
    }

    private boolean alterarPessoaJuridica(Connection conexao, PessoaJuridica pessoaJuridica) throws SQLException {
        String sqlPessoaJuridica = "UPDATE dbo.PessoasJuridicas SET CNPJ = ? WHERE IDPessoa = ?";
        try (PreparedStatement preparedPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica)) {
            preparedPessoaJuridica.setString(1, pessoaJuridica.getCnpj());
            preparedPessoaJuridica.setInt(2, pessoaJuridica.getId());
            
            return preparedPessoaJuridica.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) {
        Connection conexao = null;
        try {
            conexao = ConectorBD.getConnection();
            if (conexao == null) {
                return false;
            }
            
            conexao.setAutoCommit(false);
            
            if (!excluirPessoaJuridica(conexao, id) || !excluirPessoa(conexao, id)) {
                conexao.rollback();
                return false;
            }
            
            conexao.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir a pessoa jurídica: " + id, e);
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erro ao realizar rollback", ex);
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao fechar conexão", e);
                }
            }
        }
    }

    private boolean excluirPessoaJuridica(Connection conexao, int id) throws SQLException {
        String sqlPessoaJuridica = "DELETE FROM dbo.PessoasJuridicas WHERE IDPessoa = ?";
        try (PreparedStatement preparedPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica)) {
            preparedPessoaJuridica.setInt(1, id);
            return preparedPessoaJuridica.executeUpdate() > 0;
        }
    }

    private boolean excluirPessoa(Connection conexao, int id) throws SQLException {
        String sqlPessoa = "DELETE FROM dbo.Pessoas WHERE IDPessoa = ?";
        try (PreparedStatement preparedPessoa = ConectorBD.getPrepared(conexao, sqlPessoa)) {
            preparedPessoa.setInt(1, id);
            return preparedPessoa.executeUpdate() > 0;
        }
    }

    private PessoaJuridica criaPessoaJuridica(ResultSet resultSet) throws SQLException {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setId(resultSet.getInt("IDPessoa"));
        pessoaJuridica.setNome(resultSet.getString("NomePessoa"));
        pessoaJuridica.setTelefone(resultSet.getString("Telefone"));
        pessoaJuridica.setEmail(resultSet.getString("Email"));
        pessoaJuridica.setRua(resultSet.getString("Logradouro"));
        pessoaJuridica.setCidade(resultSet.getString("Cidade"));
        pessoaJuridica.setEstado(resultSet.getString("Estado"));
        pessoaJuridica.setCnpj(resultSet.getString("CNPJ"));
        return pessoaJuridica;
    }
}