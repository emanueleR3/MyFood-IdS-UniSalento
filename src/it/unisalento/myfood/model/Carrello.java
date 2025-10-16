package it.unisalento.myfood.model;

import java.util.HashMap;

public class Carrello {

    private HashMap<Integer, Integer> articoli;
    private Utente cliente;
    private Float totale;

    public Carrello() {
    }

    public Carrello(HashMap<Integer, Integer> articoli, Utente cliente) {
        this.articoli = articoli;
        this.cliente = cliente;
        this.totale = 0.0f;
    }

    public Utente getCliente() {
        return cliente;
    }

    public void setCliente(Utente cliente) {
        this.cliente = cliente;
    }

    public HashMap<Integer, Integer> getArticoli() {
        return articoli;
    }

    public void setArticoli(HashMap<Integer, Integer> articoli) {
        this.articoli = articoli;
    }

    public Float getTotale() {
        return totale;
    }

    public void setTotale(Float totale) {
        this.totale = totale;
    }

    @Override
    public String toString() {
        return "Carrello{" +
                "articoli=" + articoli +
                ", cliente=" + cliente +
                ", totale=" + totale +
                '}';
    }
}
