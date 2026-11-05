import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class Banco {
    private String nome;
    private ArrayList<Cliente> clientes;

    public Banco(String nome) {
        this.nome = nome;
        this.clientes = new ArrayList<>();
    }

    // Metodo pra add cliente
    //Ele adiciona clientes a bancos, usando outro metodo como condição para saber se tal cliente/CPF tem "cadastro" (Que coisa linda)
    public void addClient(Cliente c){
        if (c == null) {
            return;
        }

        if (buscarClienteCPF(c.getCPF()) == null) {
            clientes.add(c);
        }
    }

    //Metodo pra remover cliente
    public void removerClient(Cliente c){
        if (c != null && c.getContas().isEmpty()) {
            clientes.remove(c);
        }
    }

    //Metodo pra buscar cliente pelo CPF
    public Cliente buscarClienteCPF(String CPF){
        for (Cliente c : clientes) {

            if (c.getCPF().equals(CPF)) {
                return c;
            }
        }

        return null;
    }

    //Metodo pra buscar a conta pelo numero
    public Conta buscarConta(String numero){
        for (Cliente c : clientes) {
            for (Conta conta : c.getContas()) {
                if (conta.getNumero().equals(numero)) {
                    return conta;
                }
            }
        }
        return null;
    }

    //Metodo pra transferir entre contas
    public boolean transferir(Conta origem, Conta destino, double valor, LocalDate data){
        //As condicionais servem pra tirar os erros logicos das transações, ex: se não tiver endereçamento ou se o valor for menor ou igual a 0
        if (origem == null || destino == null || data == null) {
            return false;
        }
        if (origem == destino || valor <= 0) {
            return false;
        }
        //Cria a saída do tipo Transação e saída, colocando os valores, tipo, e etc
        Transacao saida = new Transacao(data, TipoTransacao.PIX_ENVIADO, valor, "PIX enviado");
        if (!origem.registrar(saida)) {
            return false;
        }
        //Cria a entrada
        Transacao entrada = new Transacao(data, TipoTransacao.PIX_RECEBIDO, valor, "PIX recebido");
        if (!destino.registrar(entrada)) {
            origem.estornarUltima();
            return false;
        }
        return true;
    }

    //Metodo pra fechar o mes, aplicando as tarifas e os rendimentos
    public void fecharMes(int ano, int mes){
        YearMonth periodo = YearMonth.of(ano, mes);
        LocalDate data = periodo.atEndOfMonth();
        for (Cliente cliente : clientes) {
            for (Conta conta : cliente.getContas()) {
                // Aplicação das regras do mês
            }
        }

    }
}
