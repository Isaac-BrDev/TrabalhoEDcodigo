
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

public class GeradorRelatorio {

    public static void gerarRelatorioClientes(Banco banco, String pasta)
            throws IOException {

        criarPasta(pasta);

        File arquivo = new File(pasta, "clientes.txt");

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(arquivo))) {

            escritor.write("RELATORIO DE CLIENTES");
            escritor.newLine();
            escritor.write("=====================");
            escritor.newLine();
            escritor.newLine();

            for (Cliente cliente : banco.getClientes()) {

                escritor.write("CPF: " + cliente.getCPF());
                escritor.newLine();

                escritor.write("Nome: " + cliente.getNome());
                escritor.newLine();

                escritor.write("Quantidade de contas: "
                        + cliente.getContas().size());
                escritor.newLine();

                escritor.write(String.format(
                        Locale.US,
                        "Saldo total: R$ %.2f",
                        cliente.saldoTotal()
                ));

                escritor.newLine();
                escritor.write("-----------------------------");
                escritor.newLine();
            }
        }
    }

    public static void gerarExtratos(Banco banco, int mes, int ano,
                                     String pasta) throws IOException {

        criarPasta(pasta);

        LocalDate primeiroDia = LocalDate.of(ano, mes, 1);

        for (Cliente cliente : banco.getClientes()) {

            for (Conta conta : cliente.getContas()) {

                String nomeArquivo = String.format(
                        "extrato_%s_%02d_%d.txt",
                        conta.getNumero(),
                        mes,
                        ano
                );

                File arquivo = new File(pasta, nomeArquivo);

                try (BufferedWriter escritor =
                             new BufferedWriter(new FileWriter(arquivo))) {

                    escritor.write("EXTRATO BANCARIO");
                    escritor.newLine();
                    escritor.write("Conta: " + conta.getNumero());
                    escritor.newLine();
                    escritor.write("Cliente: " + cliente.getNome());
                    escritor.newLine();
                    escritor.write("Periodo: " + mes + "/" + ano);
                    escritor.newLine();
                    escritor.newLine();

                    double saldo = conta.saldoEm(
                            primeiroDia.minusDays(1)
                    );

                    escritor.write(String.format(
                            Locale.US,
                            "Saldo anterior: R$ %.2f",
                            saldo
                    ));

                    escritor.newLine();
                    escritor.newLine();

                    escritor.write("TRANSACOES");
                    escritor.newLine();
                    escritor.write("-----------------------------");
                    escritor.newLine();

                    for (Transacao transacao : conta.getTransacoes()) {

                        LocalDate data = transacao.getData();

                        if (data.getMonthValue() == mes
                                && data.getYear() == ano) {

                            if (transacao.creditoOuNao()) {
                                saldo += transacao.getValor();
                            } else {
                                saldo -= transacao.getValor();
                            }

                            escritor.write(
                                    data + " | "
                                            + transacao.getTipo() + " | "
                                            + transacao.getDescricao()
                            );

                            escritor.newLine();

                            escritor.write(String.format(
                                    Locale.US,
                                    "Valor: R$ %.2f | Saldo: R$ %.2f",
                                    transacao.getValor(),
                                    saldo
                            ));

                            escritor.newLine();
                            escritor.newLine();
                        }
                    }

                    escritor.write("-----------------------------");
                    escritor.newLine();

                    escritor.write(String.format(
                            Locale.US,
                            "Saldo final: R$ %.2f",
                            saldo
                    ));
                }
            }
        }
    }

    private static void criarPasta(String caminho) throws IOException {

        File pasta = new File(caminho);

        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IOException("Nao foi possivel criar a pasta: " + caminho);
        }
    }
}
