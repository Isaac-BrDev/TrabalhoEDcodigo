import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        Transacao transacao = new Transacao(
                LocalDate.of(2026, 9, 1),
                TipoTransacao.DEPOSITO,
                1200.00,
                "Salario"
        );

        System.out.println("Tipo: " + transacao.getTipo());

        System.out.println("Valor: " + transacao.getValor());

        System.out.println("Descricao: " + transacao.getDescricao());

        System.out.println("É crédito? " + transacao.creditoOuNao());
    }
}