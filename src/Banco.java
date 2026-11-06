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

    public String getNome() {
        return nome;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
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

    // eu troquei esse metodo




    public boolean removerClient(Cliente c) {

        if (c == null || !c.getContas().isEmpty()) {
            return false;
        }

        return clientes.remove(c);
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

                TipoTransacao tipoFechamento;

                if (conta.getTipo() == TipoConta.CORRENTE) {
                    tipoFechamento = TipoTransacao.TARIFA;
                } else {
                    tipoFechamento = TipoTransacao.RENDIMENTO;
                }

                boolean fechamentoRealizado = false;

                // Verifica se o fechamento já foi realizado
                for (Transacao t : conta.getTransacoes()) {

                    YearMonth mesTransacao = YearMonth.from(t.getData());

                    if (mesTransacao.equals(periodo)
                            && t.getTipo() == tipoFechamento) {

                        fechamentoRealizado = true;
                        break;
                    }
                }

                if (fechamentoRealizado) {
                    continue;
                }

                if (conta.getTipo() == TipoConta.CORRENTE) {

                    Transacao tarifa = new Transacao(
                            data,
                            TipoTransacao.TARIFA,
                            15.00,
                            "Tarifa mensal"
                    );

                    if (!conta.registrar(tarifa)) {
                        System.out.println(
                                "Não foi possível aplicar tarifa na conta "
                                        + conta.getNumero()
                        );
                    }

                } else if (conta.getTipo() == TipoConta.POUPANCA) {

                    double saldo = conta.saldoEm(data);

                    if (saldo > 0) {

                        double rendimento =
                                Math.round(saldo * 0.005 * 100.0) / 100.0;

                        if (rendimento > 0) {

                            Transacao rendimentoMensal = new Transacao(
                                    data,
                                    TipoTransacao.RENDIMENTO,
                                    rendimento,
                                    "Rendimento mensal"
                            );

                            if (!conta.registrar(rendimentoMensal)) {
                                System.out.println(
                                        "Não foi possível aplicar rendimento na conta "
                                                + conta.getNumero()
                                );
                            }
                        }
                    }
                }
            }
        }
    }

}