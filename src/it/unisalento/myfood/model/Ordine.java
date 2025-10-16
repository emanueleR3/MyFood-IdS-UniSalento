package it.unisalento.myfood.model;

import java.sql.Timestamp;
import java.util.HashMap;

public class Ordine implements IOrdine {

    private Integer idOrdine;
    private Integer idCliente;

    private HashMap<Integer, Integer> articoli;     //HashMap<Key, Value> che nel nostro caso è HashMap<IArticolo.getId(), quantità>

    private Float totale;
    private STATO stato;
    private Timestamp data;
    private boolean ricorrente;

    public Integer getIdOrdine() {
        return idOrdine;
    }

    public HashMap<Integer, Integer> getArticoli() {
        return articoli;
    }

    public void setArticoli(HashMap<Integer, Integer> articoli) {
        this.articoli = articoli;
    }

    public boolean isRicorrente() {
        return ricorrente;
    }

    public Timestamp getData() {
        return data;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }


    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setRicorrente(boolean ricorrente) {
        this.ricorrente = ricorrente;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setStato(STATO stato) {
        this.stato = stato;
    }

    public Float getImporto() {
        return totale;
    }

    public STATO getStato() {
        return stato;
    }

    public void setTotale(Float importo) {
        this.totale = importo;
    }

    @Override
    public String toString() {
        return "Ordine{" +
                "idOrdine=" + idOrdine +
                ", idCliente=" + idCliente +
                ", articoli=" + articoli +
                ", totale=" + totale +
                ", stato=" + stato +
                ", data=" + data +
                ", ricorrente=" + ricorrente +
                '}';
    }
}

