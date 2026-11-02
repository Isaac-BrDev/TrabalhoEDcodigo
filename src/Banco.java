import java.time.LocalDate;
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
    public Conta buscarConta(String number){

        return;
    }

    //Metodo pra transferir entre contas
    public boolean transferir(Conta origem, Conta destino, double valor, LocalDate data){

        return;
    }

    //Metodo pra fechar o mes, aplicando as tarifas e os rendimentos
    public void fecharMes(int ano, int mes){

    }
}
