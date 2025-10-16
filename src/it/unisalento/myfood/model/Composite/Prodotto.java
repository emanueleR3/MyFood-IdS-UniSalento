package it.unisalento.myfood.model.Composite;

import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.ArrayList;

public class Prodotto implements IArticolo {

    private Integer id;
    private String nome;
    private String descrizione;
    private Float prezzo;
    private Integer pezziDisponibili;

    private TipologiaProdotto tipologiaProdotto;

    private ArrayList<Ingrediente> ingredienti;

    private ArrayList<String> directoryFoto;
    private ArrayList<CommentoCliente> commenti;

    public Prodotto() {
    }

    public Prodotto(String nome, String descrizione, Float prezzo, Integer pezziDisponibili, TipologiaProdotto tipologiaProdotto, ArrayList<Ingrediente> ingredienti, ArrayList<String> directoryFoto, ArrayList<CommentoCliente> commenti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.pezziDisponibili = pezziDisponibili;
        this.tipologiaProdotto = tipologiaProdotto;
        this.ingredienti = ingredienti;
        this.directoryFoto = directoryFoto;
        this.commenti = commenti;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public ArrayList<String> getDirectoryFoto() {
        return directoryFoto;
    }

    @Override
    public Integer getPezziDisponibili() {
        return pezziDisponibili;
    }

    public void setPezziDisponibili(Integer pezziDisponibili) {
        this.pezziDisponibili = pezziDisponibili;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public Float getPrezzo() {
        return prezzo;
    }

    public ArrayList<Ingrediente> getIngredienti() {
        return ingredienti;
    }

    public void setTipologiaProdotto(TipologiaProdotto tipologiaProdotto) {
        this.tipologiaProdotto = tipologiaProdotto;
    }

    public TipologiaProdotto getTipologiaProdotto() {
        return tipologiaProdotto;
    }

    @Override
    public void setDirectoryFoto(ArrayList<String> directoryFoto) {
        this.directoryFoto = directoryFoto;
    }

    @Override
    public ArrayList<CommentoCliente> getCommenti() {
        return commenti;
    }

    public void setIngredienti(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }

    @Override
    public void setCommenti(ArrayList<CommentoCliente> commenti) {
        this.commenti = commenti;
    }

    @Override
    public String toString() {
        return "Prodotto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", prezzo=" + prezzo +
                ", pezziDisponibili=" + pezziDisponibili +
                ", tipologiaProdotto=" + tipologiaProdotto +
                ", ingredienti=" + ingredienti +
                ", foto=" + directoryFoto +
                ", commenti=" + commenti +
                '}';
    }
}
