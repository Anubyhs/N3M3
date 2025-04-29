/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastrobd;

import cadastrobd.model.PessoasFisicas;
import cadastrobd.model.PessoasFisicasDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;
import java.util.Scanner;

/**
 *
 * @Francinaldo
 */
public class CadastroBD {
    private static final Scanner sc = new Scanner(System.in);
    private static final PessoasFisicasDAO pfDAO = new PessoasFisicasDAO();
    private static final PessoaJuridicaDAO pjDAO = new PessoaJuridicaDAO();


    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("===== CADASTRO BD =====");
            System.out.println("Selecione uma opcao:");
            System.out.println("1 - Incluir Pessoa");
            System.out.println("2 - Alterar Pessoa");
            System.out.println("3 - Excluir Pessoa");
            System.out.println("4 - Exibir pelo id");
            System.out.println("5 - Exibir todos");
            System.out.println("0 - Finalizar a execucao");

            opcao = Integer.parseInt(sc.nextLine());

            System.out.println("===========================================");
            switch (opcao) {
                case 1:
                    inserirPessoa();
                    break;
                case 2:
                    alterarPessoa();
                    break;
                case 3:
                    excluirPessoa();
                    break;
                case 4:
                    exibirPessoaPeloId();
                    break;
                case 5:
                    exibirTodasAsPessoas();
                    break;
                case 0:
                    System.out.println("Finalizando a execucao.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }

            System.out.println();
        }
    }

    private static String lerTipoDePessoa() {
        System.out.println("Escolha um opcao:\n\tPara Pessoa Fisica digite F\n\tPara Pessoa Juridica digite J");
        String tipo = sc.nextLine();

        System.out.println("===========================================\n");

        if (tipo.equalsIgnoreCase("F") || tipo.equalsIgnoreCase("J")) {
            return tipo;
        } else {
            System.out.println("Opcao invalida, tente novamente.");
            return lerTipoDePessoa();
        }
    }

    private static PessoasFisicas definirDadosPessoasFisicas(PessoasFisicas PessoasFisicas) {
        try {
            System.out.println("Digite o nome: ");
            PessoasFisicas.setNome(sc.nextLine());
            System.out.println("Digite o cpf: ");
            PessoasFisicas.setCpf(sc.nextLine());
            System.out.println("Digite o telefone: ");
            PessoasFisicas.setTelefone(sc.nextLine());
            System.out.println("Digite o email: ");
            PessoasFisicas.setEmail(sc.nextLine());
            System.out.println("Digite a rua: ");
            PessoasFisicas.setRua(sc.nextLine());
            System.out.println("Digite a cidade: ");
            PessoasFisicas.setCidade(sc.nextLine());
            System.out.println("Digite o estado: ");
            PessoasFisicas.setEstado(sc.nextLine());
            return PessoasFisicas;
        } catch (Exception e) {
            System.out.println("Nao foi possivel cadastrar os dados da Pessoa física:");
            e.printStackTrace();
            System.out.println("Tente novamente.");
            return null;
        }
    }

    private static PessoaJuridica definirDadosPessoaJuridica(PessoaJuridica pessoaJuridica) {
        try {
            System.out.println("Digite o nome: ");
            pessoaJuridica.setNome(sc.nextLine());
            System.out.println("Digite o CNPJ: ");
            pessoaJuridica.setCnpj(sc.nextLine());
            System.out.println("Digite o telefone: ");
            pessoaJuridica.setTelefone(sc.nextLine());
            System.out.println("Digite o email: ");
            pessoaJuridica.setEmail(sc.nextLine());
            System.out.println("Digite a rua: ");
            pessoaJuridica.setRua(sc.nextLine());
            System.out.println("Digite a cidade: ");
            pessoaJuridica.setCidade(sc.nextLine());
            System.out.println("Digite o estado: ");
            pessoaJuridica.setEstado(sc.nextLine());
            return pessoaJuridica;
        } catch (Exception e) {
            System.out.println("Nao foi possivel cadastrar os dados da Pessoa Jurídica:");
            e.printStackTrace();
            System.out.println("Tente novamente.");
            return null;
        }
    }

    private static void inserirPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            PessoasFisicas PessoasFisicas = definirDadosPessoasFisicas(new PessoasFisicas());
            if (PessoasFisicas == null) {
                System.out.println("Erro ao cadastrar Pessoa Física.");
                return;
            }
            if (!pfDAO.incluir(PessoasFisicas)) {
                System.out.println("Erro ao cadastrar Pessoa Física.");
                return;
            }
            System.out.println("Pessoa Física cadastrada com sucesso.");
            return;
        }
        if (tipo.equalsIgnoreCase("J")) {
            PessoaJuridica pessoaJuridica = definirDadosPessoaJuridica(new PessoaJuridica());
            if (pessoaJuridica == null) {
                System.out.println("Falha ao cadastrar Pessoa Jurídica.");
                return;
            }
            if (!pjDAO.incluir(pessoaJuridica)) {
                System.out.println("Falha ao cadastrar Pessoa Jurídica.");
                return;
            }
            System.out.println("Pessoa Jurídica cadastrar com sucesso.");
        }
    }

    private static void alterarPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja alterar: ");
            try {
                int idPessoasFisicas = Integer.parseInt(sc.nextLine());
                PessoasFisicas PessoasFisicas = pfDAO.getPessoa(idPessoasFisicas);

                if (PessoasFisicas == null) {
                    System.out.println("Pessoa Física não encontrada.");
                    return;
                }
                PessoasFisicas.exibir();
                PessoasFisicas = definirDadosPessoasFisicas(PessoasFisicas); // Aqui o ID já existe

                if (PessoasFisicas == null) {
                    System.out.println("Falha ao alterar Pessoa Física.");
                    return;
                }

                if (!pfDAO.alterar(PessoasFisicas)) {
                    System.out.println("Falha ao alterar Pessoa Física.");
                    return;
                }
                System.out.println("Pessoa Física alterada com sucesso.");
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja alterar: ");
            try {
                int idPessoaJuridica = Integer.parseInt(sc.nextLine());
                PessoaJuridica pessoaJuridica = pjDAO.getPessoa(idPessoaJuridica);

                if (pessoaJuridica == null) {
                    System.out.println("Pessoa Jurídica não encontrada.");
                    return;
                }

                pessoaJuridica.exibir();
                pessoaJuridica = definirDadosPessoaJuridica(pessoaJuridica); // Aqui o ID já existe

                if (pessoaJuridica == null) {
                    System.out.println("Falha ao alterar Pessoa Jurídica.");
                    return;
                }

                if (!pjDAO.alterar(pessoaJuridica)) {
                    System.out.println("Falha ao alterar Pessoa Jurídica.");
                    return;
                }

                System.out.println("Pessoa Jurídica alterada com sucesso.");
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
        }
    }

    private static void excluirPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja excluir: ");
            try {
                int idPessoasFisicas = Integer.parseInt(sc.nextLine());
                PessoasFisicas PessoasFisicas = pfDAO.getPessoa(idPessoasFisicas);

                if (PessoasFisicas == null) {
                    System.out.println("Pessoa Física não encontrada.");
                    return;
                }

                if (!pfDAO.excluir(idPessoasFisicas)) {
                    System.out.println("Falha ao excluir Pessoa Física.");
                    return;
                }

                System.out.println("Pessoa Física excluída com sucesso.");
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja excluir: ");
            try {
                int idPessoaJuridica = Integer.parseInt(sc.nextLine());
                PessoaJuridica pessoaJuridica = pjDAO.getPessoa(idPessoaJuridica);

                if (pessoaJuridica == null) {
                    System.out.println("Pessoa Jurídica não encontrada.");
                    return;
                }

                if (!pjDAO.excluir(idPessoaJuridica)) {
                    System.out.println("Falha ao excluir Pessoa Jurídica.");
                    return;
                }

                System.out.println("Pessoa Jurídica excluída com sucesso.");
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
        }
    }

    private static void exibirPessoaPeloId() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja exibir: ");
            try {
                int idPessoasFisicas = Integer.parseInt(sc.nextLine());
                PessoasFisicas PessoasFisicas = pfDAO.getPessoa(idPessoasFisicas);

                if (PessoasFisicas == null) {
                    System.out.println("Pessoa Física não encontrada.");
                    return;
                }
                PessoasFisicas.exibir();
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja exibir: ");
            try {
                int idPessoaJuridica = Integer.parseInt(sc.nextLine());
                PessoaJuridica pessoaJuridica = pjDAO.getPessoa(idPessoaJuridica);

                if (pessoaJuridica == null) {
                    System.out.println("Pessoa Jurídica não encontrada.");
                    return;
                }
                pessoaJuridica.exibir();
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número inteiro.");
            }
        }
    }

    private static void exibirTodasAsPessoas() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            java.util.List<PessoasFisicas> pessoasFisicas = pfDAO.getPessoas();
            if (pessoasFisicas == null) {
                System.out.println("Não existem Pessoas Físicas cadastradas.");
                return;
            }

            System.out.println("Exibindo todos os registros de Pessoas Físicas:\n");
            for (PessoasFisicas PessoasFisicas : pessoasFisicas) {
                System.out.println("-------------------------------------------");
                PessoasFisicas.exibir();
            }
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            java.util.List<PessoaJuridica> pessoasJuridicas = pjDAO.getPessoas();
            if (pessoasJuridicas == null) {
                System.out.println("Não existem Pessoas Jurídicas cadastradas.");
                return;
            }

            System.out.println("Exibindo todos os registros de Pessoas Jurídicas.");
            for (PessoaJuridica pessoaJuridica : pessoasJuridicas) {
                System.out.println("-------------------------------------------");
                pessoaJuridica.exibir();
            }
        }
    }
}