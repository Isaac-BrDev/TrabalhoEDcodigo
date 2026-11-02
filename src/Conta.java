import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

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

    //Metodo pra registrar uma transacao
    public boolean registrar(Transacao t){
            if(t.getTipo() == TipoTransacao.PIX_ENVIADO && t.getValor() < saldo() || t.getTipo() == TipoTransacao.SAQUE && t.getValor() < saldo()){
                return false;
            }
            transacoes.add(t);
        return true;
    }

    //Metodo pra calcular o saldo atual
    public double saldo(){
        double creditos = 0, debitos = 0;
        for(Transacao transacao : transacoes){
            if(transacao.creditoOuNao()){
                creditos += transacao.getValor();
            } else if (!transacao.creditoOuNao()) {
                debitos += transacao.getValor();
            }
        }
        return saldoInicial + creditos - debitos;
    }

    //Metodo pra calcular o saldo em determinada data
    public double saldoEm(LocalDate data){

        return;
    }

    //Metodo pra gerar o extrado mensal
    public String extrato(int ano, int mes){

        return;
    }

    //Metodo pra desfazer a ultima transacao permitida
    public Transacao estornarUltima(){

        return;
    }

    //Metodo pra retornar o limite da conta
    public double limite(){
            if(tipo == TipoConta.CORRENTE)
                return 500.0;
        return 0.0;
    }

}
