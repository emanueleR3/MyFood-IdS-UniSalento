package it.unisalento.myfood.model;

import java.util.ArrayList;

public class Ingrediente {

    private Integer id;
    private String nome;

    private TipologiaIngrediente tipologiaIngrediente;

    private Azienda produttore;
    private ArrayList<Azienda> distributori;

    public Ingrediente() {
    }

    public Ingrediente(String nome, TipologiaIngrediente tipologiaIngrediente, Azienda produttore, ArrayList<Azienda> distributori) {
        this.nome = nome;
        this.tipologiaIngrediente = tipologiaIngrediente;
        this.produttore = produttore;
        this.distributori = distributori;
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

    public TipologiaIngrediente getTipologiaIngrediente() {
        return tipologiaIngrediente;
    }

    public void setTipologiaIngrediente(TipologiaIngrediente tipologiaIngrediente) {
        this.tipologiaIngrediente = tipologiaIngrediente;
    }

    public Azienda getProduttore() {
        return produttore;
    }

    public void setProduttore(Azienda produttore) {
        this.produttore = produttore;
    }

    public ArrayList<Azienda> getDistributori() {
        return distributori;
    }

    public void setDistributori(ArrayList<Azienda> distributori) {
        this.distributori = distributori;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipologiaIngrediente=" + tipologiaIngrediente +
                ", produttore=" + produttore +
                ", distributori=" + distributori +
                '}';
    }
}
