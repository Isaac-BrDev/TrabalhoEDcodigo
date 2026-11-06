import java.util.ArrayList;

public class Cliente {
    private String CPF;
    private String nome;
    private ArrayList<Conta> contas;

    public String getCPF() {
        return CPF;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    //Construtor de Cliente
    public Cliente (String CPF, String nome){
        this.CPF = CPF;
        this.nome = nome;
        this.contas = new ArrayList<>();
    }
    //Metodo que adiciona uma conta a determinado cliente
    //Ele recebe o objeto "c" que vai ser adicionado, colocado no arraylisy "contas", o if verifica se o objeto c não é null
    public void addConta(Conta c) {

        if (c == null) {
            return;
        }

        //Percorre a lista de contas para verificar se não existe uma conta com o mesmo número da informada no método
        for (Conta conta : contas) {

            if (conta.getNumero().equals(c.getNumero())) {
                return;
            }
        }

        contas.add(c);
    }

    //Metodo que remove a conta do cliente
    //Recebe o objeto e faz o inverso do metodo anterior remove, e por ser boolean retorna que a remoção se bater as
    //Condições, no caso a condição diferente agora é a de que o saldo tem que ser 0.
    public boolean removerConta(Conta c){
        if (c != null && c.saldo() == 0){
            return contas.remove(c);
        }
        return false;
    }

    //Metodo que soma o saldo das contas do cliente
    //Cria uma variavel do tipo double pra ser retornada, percorre as contas guardando o saldo de cada objeto no total
    public double saldoTotal(){
        double total = 0;

        for (Conta c : contas) {
            total += c.saldo();
        }
        return total;
    }
}
