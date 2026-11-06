import java.time.LocalDate;
import java.util.LinkedList;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Conta {
    private String numero;
    private TipoConta tipo;
    private double saldoInicial;
    private LinkedList<Transacao> transacoes;

    //Construtor
    public Conta(String numero, TipoConta tipo, double saldoInicial){
        this.numero = numero;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        transacoes = new LinkedList<>();
    }

    //Métodos get
    public String getNumero() {
        return numero;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public LinkedList<Transacao> getTransacoes() {return transacoes;}

    //Metodo pra registrar uma transacao
    public boolean registrar(Transacao t){

        //verifica se a lista de transações não está vazia
        if(!transacoes.isEmpty()) {
            Transacao ultima = transacoes.getLast();

            //verifica se a data da transação é anterior à data da última transação
            if (t.getData().isBefore(ultima.getData())) {
                return false;
            }
        }

        //Verifica se o débito é maior que o saldo + o limite da conta
        if (t.getTipo() != TipoTransacao.TARIFA
                && !t.creditoOuNao()) {

            if (t.getValor() > saldo() + limite()) {
                return false;
            }
        }

        transacoes.add(t);

        return true;
    }

    //Metodo pra calcular o saldo atual
        public double saldo() {

            double saldoAtual = saldoInicial;

            for (Transacao transacao : transacoes) {

                if (transacao.creditoOuNao()) {
                    saldoAtual += transacao.getValor();
                } else {
                    saldoAtual -= transacao.getValor();
                }
            }

            return saldoAtual;
        }

    //Metodo pra calcular o saldo em determinada data
    public double saldoEm(LocalDate data){
        double saldoCalculado = saldoInicial;

        for (Transacao transacao : transacoes) {

            if (!transacao.getData().isAfter(data)) {

                if (transacao.creditoOuNao()) {
                    saldoCalculado += transacao.getValor();
                } else {
                    saldoCalculado -= transacao.getValor();
                }
            }
        }
        return saldoCalculado;
    }



    //Metodo pra gerar o extrado mensal

    public String extrato(int ano, int mes) {

        return extrato(ano, mes, null);
    }

    // Sobrecarga para permitir informar o nome do cliente
    public String extrato(int ano, int mes, String nomeCliente) {

        YearMonth periodo = YearMonth.of(ano, mes);

        LocalDate primeiroDia = periodo.atDay(1);
        LocalDate ultimoDia = periodo.atEndOfMonth();

        DateTimeFormatter formatoData =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Calcula o saldo anterior ao mês
        double saldoAtual = saldoEm(primeiroDia.minusDays(1));

        StringBuilder resultado = new StringBuilder();

        // Cabeçalho
        resultado.append("Extrato da conta ")
                .append(numero)
                .append(" (")
                .append(tipo)
                .append(")");

        if (nomeCliente != null && !nomeCliente.trim().isEmpty()) {
            resultado.append(" - ").append(nomeCliente);
        }

        resultado.append(String.format(
                Locale.US, " - %02d/%d%n", mes, ano
        ));

        resultado.append(String.format(
                Locale.US, "Saldo anterior: R$ %.2f%n%n", saldoAtual
        ));

        // Percorre as transações
        for (Transacao transacao : transacoes) {

            LocalDate data = transacao.getData();

            // verifica se a data da movimentação está dentro do mês solicitado.
            if (!data.isBefore(primeiroDia) && !data.isAfter(ultimoDia)) {

                double valor;

                if (transacao.creditoOuNao()) {
                    valor = transacao.getValor();
                    saldoAtual += valor;
                } else {
                    valor = -transacao.getValor();
                    saldoAtual += valor;
                }

                resultado.append(String.format(
                        Locale.US,
                        "%s %-18s %-20s %+10.2f R$ %.2f%n",
                        data.format(formatoData),
                        transacao.getTipo(),
                        transacao.getDescricao(),
                        valor,
                        saldoAtual
                ));
            }
        }

        resultado.append(String.format(
                Locale.US, "%nSaldo final: R$ %.2f%n", saldoAtual
        ));

        return resultado.toString();
    }


    //Metodo pra desfazer a ultima transacao permitida
    public Transacao estornarUltima() {

        if (transacoes.isEmpty()) {
            return null;
        }

        Transacao ultima = transacoes.getLast();

        if (ultima.getTipo() == TipoTransacao.TARIFA
                || ultima.getTipo() == TipoTransacao.RENDIMENTO) {
            return null;
        }

        transacoes.removeLast();

        return ultima;
    }

    //Metodo pra retornar o limite da conta
    public double limite(){
            if(tipo == TipoConta.CORRENTE)
                return 500.0;
        return 0.0;
    }

}
