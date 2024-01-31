package it.unisalento.myfood.model;

public class TipologiaProdotto {

    private Integer id;
    private String nome;

    public TipologiaProdotto() {}

    public TipologiaProdotto(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "TipologiaProdotto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
