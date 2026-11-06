
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EscritorDeArquivo {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void salvarBanco(Banco banco, String caminho)
            throws IOException {

        if (banco == null) {
            throw new IllegalArgumentException("O banco nao pode ser nulo.");
        }

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(caminho))) {

            // Primeira linha: nome do banco
            escritor.write(banco.getNome());
            escritor.newLine();

            // Percorre todos os clientes
            for (Cliente cliente : banco.getClientes()) {

                // CPF;nome;quantidade de contas
                escritor.write(
                        cliente.getCPF() + ";"
                                + cliente.getNome() + ";"
                                + cliente.getContas().size()
                );

                escritor.newLine();

                // Percorre as contas do cliente
                for (Conta conta : cliente.getContas()) {

                    // numero;tipo;saldoInicial;quantidade de transacoes
                    escritor.write(
                            conta.getNumero() + ";"
                                    + conta.getTipo() + ";"
                                    + String.format(
                                    Locale.US,
                                    "%.2f",
                                    conta.getSaldoInicial()
                            ) + ";"
                                    + conta.getTransacoes().size()
                    );

                    escritor.newLine();

                    // Percorre as transacoes da conta
                    for (Transacao transacao : conta.getTransacoes()) {

                        // data;tipo;valor;descricao
                        escritor.write(
                                transacao.getData().format(FORMATO_DATA) + ";"
                                        + transacao.getTipo() + ";"
                                        + String.format(
                                        Locale.US,
                                        "%.2f",
                                        transacao.getValor()
                                ) + ";"
                                        + transacao.getDescricao()
                        );

                        escritor.newLine();
                    }
                }
            }
        }
    }
}
