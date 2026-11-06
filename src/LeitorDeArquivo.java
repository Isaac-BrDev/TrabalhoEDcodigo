
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LeitorDeArquivo {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Banco carregarBanco(String caminho) throws IOException {

        try (BufferedReader leitor =
                     new BufferedReader(new FileReader(caminho))) {

            // Primeira linha: nome do banco
            String nomeBanco = lerLinha(leitor);

            if (nomeBanco == null) {
                throw new IOException("Arquivo vazio.");
            }

            Banco banco = new Banco(nomeBanco.trim());

            // Segunda etapa: leitura dos clientes
            String linha;

            while ((linha = lerLinha(leitor)) != null) {

                String[] dadosCliente = separarCampos(linha, 3);

                String cpf = dadosCliente[0];
                String nome = dadosCliente[1];
                int quantidadeContas =
                        Integer.parseInt(dadosCliente[2]);

                if (quantidadeContas < 0) {
                    throw new IOException("Quantidade de contas inválida.");
                }

                Cliente cliente = new Cliente(cpf, nome);

                // Leitura das contas do cliente
                for (int i = 0; i < quantidadeContas; i++) {

                    String linhaConta = lerLinha(leitor);

                    if (linhaConta == null) {
                        throw new IOException("Arquivo terminou durante a leitura das contas.");
                    }

                    String[] dadosConta = separarCampos(linhaConta, 4);

                    String numero = dadosConta[0];

                    TipoConta tipo =
                            TipoConta.valueOf(dadosConta[1].toUpperCase());

                    double saldoInicial =
                            Double.parseDouble(dadosConta[2]);

                    int quantidadeTransacoes =
                            Integer.parseInt(dadosConta[3]);

                    if (quantidadeTransacoes < 0) {
                        throw new IOException("Quantidade de transações inválida.");
                    }

                    Conta conta = new Conta(
                            numero,
                            tipo,
                            saldoInicial
                    );

                    // Leitura das transações da conta
                    for (int j = 0; j < quantidadeTransacoes; j++) {

                        String linhaTransacao = lerLinha(leitor);

                        if (linhaTransacao == null) {
                            throw new IOException("Arquivo terminou durante a leitura das transações.");
                        }

                        String[] dadosTransacao =
                                separarCampos(linhaTransacao, 4);

                        LocalDate data = LocalDate.parse(
                                dadosTransacao[0],
                                FORMATO_DATA
                        );

                        TipoTransacao tipoTransacao =
                                TipoTransacao.valueOf(
                                        dadosTransacao[1].toUpperCase()
                                );

                        double valor =
                                Double.parseDouble(dadosTransacao[2]);

                        String descricao = dadosTransacao[3];

                        Transacao transacao = new Transacao(
                                data,
                                tipoTransacao,
                                valor,
                                descricao
                        );

                        // Registra a transação na conta
                        if (!conta.registrar(transacao)) {
                            throw new IOException(
                                    "Transação inválida na conta " + numero
                            );
                        }
                    }

                    // Adiciona a conta ao cliente
                    cliente.addConta(conta);
                }

                // Adiciona o cliente ao banco
                banco.addClient(cliente);
            }

            return banco;

        } catch (IllegalArgumentException e) {
            throw new IOException(
                    "Erro ao interpretar os dados do arquivo: " + e.getMessage(),
                    e
            );
        }
    }

    // Lê uma linha e ignora linhas vazias
    private static String lerLinha(BufferedReader leitor)
            throws IOException {

        String linha;

        while ((linha = leitor.readLine()) != null) {

            if (!linha.trim().isEmpty()) {
                return linha.trim();
            }
        }

        return null;
    }

    // Separa os campos e remove espaços desnecessários
    private static String[] separarCampos(String linha, int quantidadeEsperada)
            throws IOException {

        String[] campos = linha.split(";", -1);

        if (campos.length != quantidadeEsperada) {
            throw new IOException("Formato de linha inválido: " + linha);
        }

        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].trim();
        }

        return campos;
    }
}
