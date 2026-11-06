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

        if (buscarClienteCPF(c.getCPF()) !=null) {
            return;
        }
        // Verifica se alguma conta já pertence a outro cliente
        for (Conta novaConta : c.getContas()) {

            if (buscarConta(novaConta.getNumero()) != null) {
                return;
            }
        }

        // Inserção ordenada pelo nome
        int posicao = 0;

        while (posicao < clientes.size() && clientes.get(posicao).getNome().compareToIgnoreCase(c.getNome()) < 0) {

            posicao++;
        }

        clientes.add(posicao, c);
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

        // Verifica se as duas contas pertencem a este banco
        if (buscarConta(origem.getNumero()) != origem
                || buscarConta(destino.getNumero()) != destino) {
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

    public void fecharMes(int ano, int mes) {

        YearMonth periodo = YearMonth.of(ano, mes);

        LocalDate data = periodo.atEndOfMonth();

        for (Cliente cliente : clientes) {

            for (Conta conta : cliente.getContas()) {

                boolean fechamentoRealizado = false;

                // Verifica se já existe tarifa ou rendimento no mês
                for (Transacao t : conta.getTransacoes()) {

                    YearMonth mesTransacao =
                            YearMonth.from(t.getData());

                    if (mesTransacao.equals(periodo)
                            && (t.getTipo() == TipoTransacao.TARIFA
                            || t.getTipo() == TipoTransacao.RENDIMENTO)) {

                        fechamentoRealizado = true;
                        break;
                    }
                }

                // Evita duplicar o fechamento
                if (fechamentoRealizado) {
                    continue;
                }

                if (conta.getTipo() == TipoConta.CORRENTE) {

                    // Tarifa mensal de R$ 15,00
                    Transacao tarifa = new Transacao(
                            data,
                            TipoTransacao.TARIFA,
                            15.00,
                            "Tarifa mensal"
                    );

                    conta.registrar(tarifa);

                } else if (conta.getTipo() == TipoConta.POUPANCA) {

                    // Calcula o saldo no último dia do mês
                    double saldo = conta.saldoEm(data);

                    if (saldo > 0) {

                        // Rendimento de 0,5%, arredondado para duas casas
                        double rendimento =
                                Math.round(saldo * 0.005 * 100.0) / 100.0;

                        if (rendimento > 0) {

                            Transacao rendimentoMensal = new Transacao(
                                    data,
                                    TipoTransacao.RENDIMENTO,
                                    rendimento,
                                    "Rendimento mensal"
                            );

                            conta.registrar(rendimentoMensal);
                        }
                    }
                }
            }
        }
    }

}
