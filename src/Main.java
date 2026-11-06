import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {

            Banco banco = LeitorDeArquivo.carregarBanco("dados/banco.txt");

            System.out.println("Banco carregado com sucesso!");

            GeradorRelatorio.gerarRelatorioClientes(banco, "saidas");

            GeradorRelatorio.gerarExtratos(banco, 9, 2026, "saidas");

            System.out.println("Relatorios gerados com sucesso!");

        } catch (IOException e) {

            System.out.println("Erro: " + e.getMessage());

        }
    }
}