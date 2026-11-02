import java.time.LocalDate;

public class Transacao {
    private LocalDate data;
    private TipoTransacao tipoT;
    private Double valor;
    private String descricao;

    //Construtor
    public Transacao(LocalDate data, TipoTransacao tipoT, double valor, String descricao){
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }

        if (tipoT == null) {
            throw new IllegalArgumentException("O tipo não pode ser nulo.");
        }

        if (descricao == null) {
            throw new IllegalArgumentException("A descrição não pode ser nula.");
        }

        this.data = data;
        this.tipoT = tipoT;
        this.valor = valor;
        this.descricao = descricao;
    }

    //Métodos get
    public LocalDate getData() {
        return data;
    }

    public TipoTransacao getTipo() {
        return tipoT;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    //Metodo verificar se a transacao é um credito
    public Boolean creditoOuNao() {
        return tipoT == TipoTransacao.DEPOSITO || tipoT == TipoTransacao.PIX_RECEBIDO || tipoT == TipoTransacao.RENDIMENTO;
    }

}
