import java.util.Locale;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;  //quando o texto não pode ser convertido para uma data
import java.time.format.ResolverStyle;  //exige uma data realmente válida, tipo 31/02/2026
import java.io.IOException;


public class Main {
    public static void buscarEntidade(Scanner sc, Banco banco) {

        int opcao;

        do {

            System.out.println("\n========== BUSCAR ENTIDADE ==========");
            System.out.println("1 - Buscar cliente pelo CPF");
            System.out.println("2 - Buscar conta pelo numero");
            System.out.println("3 - Voltar");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:

                    String cpf = lerCPF(sc, "Digite o CPF: ");

                    Cliente cliente = banco.buscarClienteCPF(cpf);

                    if (cliente == null) {

                        System.out.println("ERRO: Cliente nao encontrado!");

                    } else {

                        System.out.println("\nCliente encontrado!");
                        System.out.println("Nome: " + cliente.getNome());
                        System.out.println("CPF: " + cliente.getCPF());

                        System.out.println("Quantidade de contas: "
                                + cliente.getContas().size());

                        System.out.printf(Locale.US,
                                "Saldo total: R$ %.2f%n",
                                cliente.saldoTotal());
                    }

                    break;

                case 2:

                    String numero = lerTexto(sc, "Digite o numero da conta: ");

                    Conta conta = banco.buscarConta(numero);

                    if (conta == null) {

                        System.out.println("ERRO: Conta nao encontrada!");

                    } else {

                        System.out.println("\nConta encontrada!");
                        System.out.println("Numero: " + conta.getNumero());
                        System.out.println("Tipo: " + conta.getTipo());

                        System.out.printf(Locale.US,
                                "Saldo atual: R$ %.2f%n",
                                conta.saldo());
                    }

                    break;

                case 3:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 3);
    }


    public static void menuRelatorios(Scanner sc, Banco banco) {

        int opcao;

        do {

            System.out.println("\n========== RELATORIOS ==========");
            System.out.println("1 - Gerar relatorio de clientes");
            System.out.println("2 - Gerar extratos mensais");
            System.out.println("3 - Voltar");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:

                    try {

                        GeradorRelatorio.gerarRelatorioClientes(
                                banco,
                                "saidas"
                        );

                        System.out.println(
                                "Relatorio de clientes gerado com sucesso!"
                        );

                        System.out.println(
                                "Arquivo: saidas/clientes.txt"
                        );

                    } catch (IOException e) {

                        System.out.println(
                                "Erro ao gerar relatorio: " + e.getMessage()
                        );
                    }

                    break;

                case 2:

                    int mes = lerInteiro(sc, "Digite o mes (1 a 12): ");

                    if (mes < 1 || mes > 12) {
                        System.out.println("ERRO: Mes invalido!");
                        break;
                    }

                    int ano = lerInteiro(sc, "Digite o ano: ");

                    if (ano < 1 || ano > 9999) {
                        System.out.println("ERRO: Ano invalido!");
                        break;
                    }

                    try {

                        GeradorRelatorio.gerarExtratos(
                                banco,
                                mes,
                                ano,
                                "saidas"
                        );

                        System.out.println(
                                "Extratos gerados com sucesso!"
                        );

                        System.out.println(
                                "Verifique a pasta saidas."
                        );

                    } catch (IOException e) {

                        System.out.println(
                                "Erro ao gerar extratos: " + e.getMessage()
                        );
                    }

                    break;

                case 3:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 3);
    }



    public static void salvarDados(Banco banco) {

        try {

            EscritorDeArquivo.salvarBanco(
                    banco,
                    "dados/banco.txt"
            );

            System.out.println("\nDados salvos com sucesso!");

        } catch (IOException e) {

            System.out.println("\nErro ao salvar os dados:");
            System.out.println(e.getMessage());

        }
    }



    public static void consultarExtrato(Scanner sc, Banco banco) {

        System.out.println("\n========== CONSULTAR EXTRATO ==========");

        String numero = lerTexto(sc, "Digite o numero da conta: ");

        Conta conta = banco.buscarConta(numero);

        if (conta == null) {
            System.out.println("ERRO: Conta nao encontrada!");
            return;
        }

        int ano = lerInteiro(sc, "Digite o ano: ");

        while (ano < 1 || ano > 9999) {
            System.out.println("ERRO: Ano invalido!");
            ano = lerInteiro(sc, "Digite o ano: ");
        }

        int mes = lerInteiro(sc, "Digite o mes (1 a 12): ");

        while (mes < 1 || mes > 12) {
            System.out.println("ERRO: Mes invalido!");
            mes = lerInteiro(sc, "Digite o mes (1 a 12): ");
        }

        System.out.println("\n" + conta.extrato(ano, mes));
    }
    public static void fecharMes(Scanner sc, Banco banco) {

        System.out.println("\n========== FECHAMENTO DO MES ==========");

        int ano = lerInteiro(sc, "Digite o ano: ");

        while (ano < 1 || ano > 9999) {
            System.out.println("ERRO: Ano invalido!");
            ano = lerInteiro(sc, "Digite o ano: ");
        }

        int mes = lerInteiro(sc, "Digite o mes (1 a 12): ");

        while (mes < 1 || mes > 12) {
            System.out.println("ERRO: Mes invalido!");
            mes = lerInteiro(sc, "Digite o mes (1 a 12): ");
        }

        if (confirmar(sc, "Deseja realizar o fechamento deste mes?")) {

            banco.fecharMes(ano, mes);

            System.out.println("Fechamento do mes solicitado com sucesso!");

        } else {

            System.out.println("Operacao cancelada.");
        }
    }

    public static void listarContasNegativas(Banco banco) {

        boolean encontrou = false;

        for (Cliente cliente : banco.getClientes()) {

            for (Conta conta : cliente.getContas()) {

                if (conta.saldo() < 0) {

                    System.out.println("\n------------------------------");
                    System.out.println("TITULAR: " + cliente.getNome());
                    System.out.println("CPF: " + cliente.getCPF());
                    System.out.println("CONTA: " + conta.getNumero());
                    System.out.println("TIPO: " + conta.getTipo());

                    System.out.printf(
                            Locale.US,
                            "SALDO: R$ %.2f%n",
                            conta.saldo()
                    );

                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("\nNenhuma conta com saldo negativo.");
        }
    }

    public static void realizarTransferencia(Scanner sc, Banco banco) {

        System.out.println("\n========== TRANSFERENCIA PIX ==========");

        String numeroOrigem = lerTexto(sc, "Conta de origem: ");

        Conta origem = banco.buscarConta(numeroOrigem);

        if (origem == null) {
            System.out.println("ERRO: Conta de origem nao encontrada!");
            return;
        }

        String numeroDestino = lerTexto(sc, "Conta de destino: ");

        Conta destino = banco.buscarConta(numeroDestino);

        if (destino == null) {
            System.out.println("ERRO: Conta de destino nao encontrada!");
            return;
        }

        if (origem == destino) {
            System.out.println("ERRO: A origem e o destino nao podem ser iguais!");
            return;
        }

        double valor = lerDouble(sc, "Digite o valor da transferencia: R$ ");

        LocalDate data = lerData(sc, "Digite a data (dd/MM/aaaa): ");

        if (confirmar(sc, "Confirmar transferencia?")) {

            if (banco.transferir(origem, destino, valor, data)) {

                System.out.println("Transferencia realizada com sucesso!");

            } else {

                System.out.println("ERRO: Nao foi possivel realizar a transferencia.");
            }

        } else {

            System.out.println("Transferencia cancelada.");
        }
    }
    public static void registrarMovimentacao(
            Scanner sc, Banco banco, TipoTransacao tipo) {

        System.out.println("\n========== " + tipo + " ==========");

        String numero = lerTexto(sc, "Digite o numero da conta: ");

        Conta conta = banco.buscarConta(numero);

        if (conta == null) {
            System.out.println("ERRO: Conta nao encontrada!");
            return;
        }

        double valor = lerDouble(sc, "Digite o valor: R$ ");

        LocalDate data = lerData(sc, "Digite a data (dd/MM/aaaa): ");

        String descricao = lerTexto(sc, "Digite a descricao: ");

        Transacao transacao = new Transacao(
                data, tipo, valor, descricao
        );

        if (conta.registrar(transacao)) {

            System.out.println("Operacao realizada com sucesso!");

            System.out.printf(Locale.US,
                    "Saldo atualizado: R$ %.2f%n", conta.saldo());

        } else {

            System.out.println("ERRO: Operacao recusada. Verifique o saldo e o limite da conta.");
        }
    }
    public static void menuOperacoesBancarias(Scanner sc, Banco banco) {

        int opcao;

        do {

            System.out.println("\n========== OPERACOES BANCARIAS ==========");
            System.out.println("1 - Deposito");
            System.out.println("2 - Saque");
            System.out.println("3 - Transferencia PIX");
            System.out.println("4 - Fechar mes");
            System.out.println("5 - Consultar extrato");
            System.out.println("6 - Estornar ultima transacao");
            System.out.println("7 - Voltar");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:
                    registrarMovimentacao(sc, banco, TipoTransacao.DEPOSITO);
                    break;

                case 2:
                    registrarMovimentacao(sc, banco, TipoTransacao.SAQUE);
                    break;

                case 3:
                    realizarTransferencia(sc, banco);
                    break;

                case 4:
                    fecharMes(sc, banco);
                    break;

                case 5:
                    consultarExtrato(sc, banco);
                    break;

                case 6:
                    removerUltimaTransacao(sc, banco);
                    break;

                case 7:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 7);
    }

    public static void removerUltimaTransacao(Scanner sc, Banco banco) {

        System.out.println("\n========== REMOVER TRANSACAO ==========");

        String numero = lerTexto(sc, "Digite o numero da conta: ");

        Conta conta = banco.buscarConta(numero);

        if (conta == null) {
            System.out.println("ERRO: Conta nao encontrada!");
            return;
        }

        if (conta.getTransacoes().isEmpty()) {
            System.out.println("ERRO: Esta conta nao possui transacoes.");
            return;
        }

        Transacao ultima = conta.getTransacoes().getLast();

        System.out.println("\nUltima transacao:");
        System.out.println("Tipo: " + ultima.getTipo());
        System.out.println("Data: " + ultima.getData());

        System.out.printf(Locale.US,
                "Valor: R$ %.2f%n", ultima.getValor());

        if (confirmar(sc, "Deseja realmente remover esta transacao?")) {

            Transacao removida = conta.estornarUltima();

            if (removida != null) {
                System.out.println("Ultima transacao removida com sucesso!");
            } else {
                System.out.println("ERRO: Nao foi possivel remover a transacao.");
            }

        } else {

            System.out.println("Remocao cancelada.");
        }
    }
    public static void removerConta(Scanner sc, Banco banco) {

        System.out.println("\n========== REMOVER CONTA ==========");

        String numero = lerTexto(sc, "Digite o numero da conta: ");

        Conta conta = banco.buscarConta(numero);

        if (conta == null) {
            System.out.println("ERRO: Conta nao encontrada!");
            return;
        }

        Cliente titular = null;

        for (Cliente cliente : banco.getClientes()) {

            if (cliente.getContas().contains(conta)) {
                titular = cliente;
                break;
            }
        }

        if (titular == null) {
            System.out.println("ERRO: Titular da conta nao encontrado!");
            return;
        }

        if (conta.saldo() != 0.0) {
            System.out.println("ERRO: A conta precisa possuir saldo zero para ser removida.");
            return;
        }

        if (confirmar(sc, "Deseja realmente remover a conta " + numero + "?")) {

            if (titular.removerConta(conta)) {
                System.out.println("Conta removida com sucesso!");
            } else {
                System.out.println("ERRO: Nao foi possivel remover a conta.");
            }

        } else {

            System.out.println("Remocao cancelada.");
        }
    }
    public static void menuRemoverElemento(Scanner sc, Banco banco) {

        int opcao;

        do {

            System.out.println("\n========== REMOVER ELEMENTO ==========");
            System.out.println("1 - Remover conta");
            System.out.println("2 - Remover ultima transacao");
            System.out.println("3 - Voltar");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:
                    removerConta(sc, banco);
                    break;

                case 2:
                    removerUltimaTransacao(sc, banco);
                    break;

                case 3:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 3);
    }
    public static void removerCliente(Scanner sc, Banco banco) {

        System.out.println("\n========== REMOVER CLIENTE ==========");

        String cpf = lerCPF(sc, "Digite o CPF do cliente: ");

        Cliente cliente = banco.buscarClienteCPF(cpf);

        if (cliente == null) {

            System.out.println("ERRO: Cliente nao encontrado!");
            return;
        }

        if (!cliente.getContas().isEmpty()) {

            System.out.println("ERRO: Este cliente ainda possui contas.");
            System.out.println("Remova as contas antes de remover o cliente.");
            return;
        }

        if (confirmar(sc, "Deseja realmente remover " + cliente.getNome() + "?")) {

            if (banco.removerClient(cliente)) {

                System.out.println("Cliente removido com sucesso!");

            } else {

                System.out.println("ERRO: Nao foi possivel remover o cliente.");
            }

        } else {

            System.out.println("Remocao cancelada.");
        }
    }
    public static double lerSaldoInicial(Scanner sc) {

        while (true) {

            System.out.print("Digite o saldo inicial: R$ ");

            String entrada = sc.nextLine();

            try {

                double valor = Double.parseDouble(entrada);

                if (!Double.isFinite(valor) || valor < 0) {

                    System.out.println("ERRO: O saldo nao pode ser negativo!");
                    continue;
                }

                return valor;

            } catch (NumberFormatException e) {

                System.out.println("ERRO: Digite um valor numerico valido!");
            }
        }
    }
    public static TipoConta lerTipoConta(Scanner sc) {

        while (true) {

            System.out.println("\nEscolha o tipo de conta:");
            System.out.println("1 - Conta corrente");
            System.out.println("2 - Conta poupanca");

            int opcao = lerInteiro(sc, "Opcao: ");

            switch (opcao) {

                case 1:
                    return TipoConta.CORRENTE;

                case 2:
                    return TipoConta.POUPANCA;

                default:
                    System.out.println("ERRO: Tipo de conta inexistente!");
            }
        }
    }
    public static void adicionarConta(Scanner sc, Banco banco) {

        System.out.println("\n========== ADICIONAR CONTA ==========");

        String cpf = lerCPF(sc, "Digite o CPF do cliente: ");

        Cliente cliente = banco.buscarClienteCPF(cpf);

        if (cliente == null) {
            System.out.println("ERRO: Cliente nao encontrado!");
            return;
        }

        String numero;

        while (true) {

            numero = lerTexto(sc, "Digite o numero da conta: ");

            if (banco.buscarConta(numero) == null) {
                break;
            }

            System.out.println("ERRO: Esse numero de conta ja esta cadastrado!");
        }

        TipoConta tipo = lerTipoConta(sc);

        double saldoInicial = lerSaldoInicial(sc);

        Conta conta = new Conta(numero, tipo, saldoInicial);

        cliente.addConta(conta);

        System.out.println("\nConta adicionada com sucesso!");
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Numero: " + conta.getNumero());
        System.out.println("Tipo: " + conta.getTipo());

        System.out.printf(Locale.US,
                "Saldo inicial: R$ %.2f%n", conta.getSaldoInicial());
    }
    public static void detalharConta(Scanner sc, Banco banco) {

        System.out.println("\n========== DETALHES DA CONTA ==========");

        String numero = lerTexto(sc, "Digite o numero da conta: ");

        Conta conta = banco.buscarConta(numero);

        if (conta == null) {

            System.out.println("ERRO: Conta nao encontrada!");
            return;
        }

        System.out.println("\nNumero: " + conta.getNumero());
        System.out.println("Tipo: " + conta.getTipo());

        System.out.printf(Locale.US,
                "Saldo inicial: R$ %.2f%n", conta.getSaldoInicial());

        System.out.printf(Locale.US,
                "Saldo atual: R$ %.2f%n", conta.saldo());

        System.out.printf(Locale.US,
                "Limite: R$ %.2f%n", conta.limite());

        System.out.println("\n-------------- TRANSACOES --------------");

        if (conta.getTransacoes().isEmpty()) {

            System.out.println("Nenhuma transacao registrada.");

        } else {

            for (Transacao t : conta.getTransacoes()) {

                System.out.printf(Locale.US,
                        "%s | %-15s | R$ %10.2f | %s%n",
                        t.getData(),
                        t.getTipo(),
                        t.getValor(),
                        t.getDescricao());
            }
        }
    }
public static void detalharCliente(Scanner sc, Banco banco) {

    System.out.println("\n========== DETALHES DO CLIENTE ==========");

    String cpf = lerTexto(sc, "Digite o CPF do cliente: ");

    Cliente cliente = banco.buscarClienteCPF(cpf);

    if (cliente == null) {

        System.out.println("ERRO: Cliente nao encontrado!");
        return;
    }

    System.out.println("\nNome: " + cliente.getNome());
    System.out.println("CPF: " + cliente.getCPF());

    System.out.printf(Locale.US,
            "Saldo total: R$ %.2f%n",
            cliente.saldoTotal());

    System.out.println("\n--------------- CONTAS ----------------");

    if (cliente.getContas().isEmpty()) {

        System.out.println("Este cliente nao possui contas.");

    } else {

        for (Conta conta : cliente.getContas()) {

            System.out.printf(Locale.US,
                    "Numero: %s | Tipo: %s | Saldo: R$ %.2f%n",
                    conta.getNumero(),
                    conta.getTipo(),
                    conta.saldo());
        }
    }
}
    public static void detalharEntidade(Scanner sc, Banco banco) {

        int opcao;

        do {

            System.out.println("\n========== DETALHAR ENTIDADE ==========");
            System.out.println("1 - Detalhar cliente");
            System.out.println("2 - Detalhar conta");
            System.out.println("3 - Voltar");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:
                    detalharCliente(sc, banco);
                    break;

                case 2:
                    detalharConta(sc, banco);
                    break;

                case 3:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 3);
    }
    public static void listarClientesEContas(Banco banco) {

        System.out.println("\n========== CLIENTES E CONTAS ==========");
        /*   %-10s: texto alinhado à esquerda, ocupando 10 posições.
        %.2f: número decimal com duas casas.
        %n: quebra de linha.
         */
        if (banco.getClientes().isEmpty()) {

            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        int totalContas = 0;

        for (Cliente cliente : banco.getClientes()) {

            System.out.println("\n----------------------------------------");

            System.out.println("Nome: " + cliente.getNome());
            System.out.println("CPF: " + cliente.getCPF());

            System.out.println("Quantidade de contas: "
                    + cliente.getContas().size());

            System.out.printf(Locale.US,
                    "Saldo total: R$ %.2f%n",
                    cliente.saldoTotal());

            if (cliente.getContas().isEmpty()) {

                System.out.println("Este cliente nao possui contas.");

            } else {

                System.out.println("\nContas:");

                for (Conta conta : cliente.getContas()) {

                    System.out.printf(Locale.US,
                            "Numero: %-10s | Tipo: %-10s | Saldo: R$ %.2f%n",
                            conta.getNumero(),
                            conta.getTipo(),
                            conta.saldo());

                    totalContas++;
                }
            }
        }

        System.out.println("\n----------------------------------------");
        System.out.println("Total de clientes: " + banco.getClientes().size());
        System.out.println("Total de contas: " + totalContas);
        System.out.println("========================================");
    }
    public static String lerCPF(Scanner sc, String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String cpf = sc.nextLine().trim();

            if (cpf.matches("[0-9]{11}")) { //[0-9] significa que são aceitos números de 0 até 9. {11} significa que devem existir exatamente 11 caracteres numéricos.
                // a entrada 11111111111 é aceita e a 111.111.111-11 n
                return cpf;

            } else {

                System.out.println("ERRO: O CPF deve conter exatamente 11 numeros!");
            }
        }
    }
    public static void cadastrarCliente(Scanner sc, Banco banco) {

        System.out.println("\n===== CADASTRO DE CLIENTE =====");

        String cpf = lerCPF(sc, "Digite o CPF: ");

        if (banco.buscarClienteCPF(cpf) != null) {
            System.out.println("ERRO: CPF ja cadastrado!");
            return;
        }
        // a linha abaixo o lertexto verifica se esta vazio
        String nome = lerTexto(sc, "Digite o nome: ");

        Cliente cliente = new Cliente(cpf, nome);

        banco.addClient(cliente);

        System.out.println("Cliente cadastrado com sucesso!");
    }
    public static String lerTexto(Scanner sc, String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String entrada = sc.nextLine();

            if (!entrada.trim().isEmpty()) { //verifica se o texto não está vazio depois da remoção dos espaços

                return entrada.trim();  //Remove os espaços      do início e do final do texto.

            } else {

                System.out.println("ERRO: Este campo nao pode ficar vazio!");
            }
        }
    }
    public static LocalDate lerData(Scanner sc, String mensagem) {

        DateTimeFormatter formato = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {

            System.out.print(mensagem);

            String entrada = sc.nextLine();

            try {

                return LocalDate.parse(entrada, formato);

            } catch (DateTimeParseException e) {

                System.out.println("ERRO: Digite uma data valida (dd/MM/aaaa)!");
            }
        }
    }
    public static boolean confirmar(Scanner sc, String mensagem) {

        while (true) {

            System.out.print(mensagem + " (s/n): ");

            String resposta = sc.nextLine().trim();

            if (resposta.equalsIgnoreCase("s")) {  // equalsignorecase Compara textos ignorando a diferença entre letras maiúsculas e minúsculas
                return true;
            }

            if (resposta.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("ERRO: Digite apenas s ou n!");
        }
    }

    public static int lerInteiro(Scanner sc, String mensagem) {
        //Em vez de encerrar o programa com uma exceção, informa o erro (letra no lugar de numero) e pede pra escolher uma opção novamente.
        while (true) { // mantem a leitura enquanto n recebe uma entrada valida

            System.out.print(mensagem);

            String entrada = sc.nextLine();

            try {
                //try-catch: tenta converter o texto em int (text 15= int 15) e captura o erro caso a conversão não seja possível.
                return Integer.parseInt(entrada);

            } catch (NumberFormatException e) {
                // Código executado caso a exceção aconteça.
                System.out.println("ERRO: Digite um numero valido!");

                // qualquer numero int é valido, mas cai no default, letras e o resto não ent ele repete a pergunta.
            }
        }
    }
    public static double lerDouble(Scanner sc, String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String entrada = sc.nextLine();

            try { // igual o try-catch do lerint

                double valor = Double.parseDouble(entrada);

                if (!Double.isFinite(valor) || valor <= 0) {  // ve se é um valor finito
                    System.out.println("ERRO: Digite um valor positivo!");
                    continue;
                }

                return valor;

            } catch (NumberFormatException e) {

                System.out.println("ERRO: Digite um valor numerico valido!");
            }
        }
    }

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        Scanner sc = new Scanner(System.in);

        Banco banco;

        // Carrega os dados do arquivo
        try {

            banco = LeitorDeArquivo.carregarBanco("dados/grande.txt");

            System.out.println("==============================================");
            System.out.println("       BANCO CARREGADO COM SUCESSO!");
            System.out.println("==============================================");
            System.out.println("Banco: " + banco.getNome());
            System.out.println("Clientes cadastrados: " + banco.getClientes().size());

        } catch (IOException e) {

            System.out.println("ERRO AO CARREGAR O BANCO!");
            System.out.println(e.getMessage());

            sc.close();
            return;
        }

        int opcao;

        do {

            System.out.println("\n==============================================");
            System.out.println("               SISTEMA BANCARIO");
            System.out.println("==============================================");

            System.out.println("1  - Listar clientes e contas");
            System.out.println("2  - Detalhar cliente ou conta");
            System.out.println("3  - Cadastrar cliente");
            System.out.println("4  - Adicionar conta");
            System.out.println("5  - Remover cliente");
            System.out.println("6  - Remover conta ou transacao");
            System.out.println("7  - Buscar por CPF ou numero da conta");
            System.out.println("8  - Operacoes bancarias");
            System.out.println("9  - Gerar relatorios");
            System.out.println("10 - Listar contas com saldo negativo");
            System.out.println("11 - Salvar dados");
            System.out.println("12 - Sair");

            System.out.println("----------------------------------------------");

            opcao = lerInteiro(sc, "Escolha uma opcao: ");

            switch (opcao) {

                case 1:
                    listarClientesEContas(banco);
                    break;

                case 2:
                    detalharEntidade(sc, banco);
                    break;

                case 3:
                    cadastrarCliente(sc, banco);
                    break;

                case 4:
                    adicionarConta(sc, banco);
                    break;

                case 5:
                    removerCliente(sc, banco);
                    break;

                case 6:
                    menuRemoverElemento(sc, banco);
                    break;

                case 7:
                    buscarEntidade(sc, banco);
                    break;

                case 8:
                    menuOperacoesBancarias(sc, banco);
                    break;

                case 9:
                    menuRelatorios(sc, banco);
                    break;

                case 10:
                    listarContasNegativas(banco);
                    break;

                case 11:
                    salvarDados(banco);
                    break;

                case 12:

                    if (confirmar(sc, "Deseja salvar os dados antes de sair?")) {
                        salvarDados(banco);
                    } else {
                        System.out.println("Saindo sem salvar os dados.");
                    }

                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("ERRO: Opcao inexistente!");
            }

        } while (opcao != 12);

        sc.close();
    }

}