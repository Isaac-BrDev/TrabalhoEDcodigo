import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        Banco banco = new Banco("Banco Digital");

        Cliente cliente = new Cliente(
                "11122233344",
                "Francisca Almeida"
        );

        Conta corrente = new Conta(
                "1001",
                TipoConta.CORRENTE,
                1000.00
        );

        Conta poupanca = new Conta(
                "1002",
                TipoConta.POUPANCA,
                2000.00
        );

        cliente.addConta(corrente);
        cliente.addConta(poupanca);

        banco.addClient(cliente);

        // Primeiro fechamento
        banco.fecharMes(2026, 9);

        System.out.println("Saldo da corrente: R$ "
                + corrente.saldo());

        System.out.println("Saldo da poupanca: R$ "
                + poupanca.saldo());

        // Segundo fechamento: não deve duplicar operações
        banco.fecharMes(2026, 9);

        System.out.println("\nDepois do segundo fechamento:");

        System.out.println("Saldo da corrente: R$ "
                + corrente.saldo());

        System.out.println("Saldo da poupanca: R$ "
                + poupanca.saldo());
    }
}