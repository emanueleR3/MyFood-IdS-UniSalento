package it.unisalento.myfood.model.Composite;

import it.unisalento.myfood.model.Utente;

import java.sql.Timestamp;

public class CommentoCliente implements IInterazioneUtente {

    private Integer idCommento;
    private Utente cliente;
    private Integer idArticolo;

    private String testo;

    private INDICE_GRADIMENTO indiceDiGradimento;

    private Timestamp dataEOra;

    public CommentoCliente() {
        this.testo = null;      // perchè nel db potrebbe essere null;
    }

    public CommentoCliente(Utente cliente, Integer idArticolo, String testo, INDICE_GRADIMENTO indiceDiGradimento, Timestamp dataEOra) {
        this.cliente = cliente;
        this.idArticolo = idArticolo;
        this.testo = testo;
        this.indiceDiGradimento = indiceDiGradimento;
        this.dataEOra = dataEOra;
    }

    public void setIdCommento(Integer idCommento) {
        this.idCommento = idCommento;
    }

    public void setCliente(Utente cliente) {
        this.cliente = cliente;
    }

    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setIndiceDiGradimento(INDICE_GRADIMENTO indiceDiGradimento) {
        this.indiceDiGradimento = indiceDiGradimento;
    }

    public void setDataEOra(Timestamp dataEOra) {
        this.dataEOra = dataEOra;
    }

    public Integer getIdArticolo() {
        return idArticolo;
    }

    public INDICE_GRADIMENTO getIndiceDiGradimento() {
        return indiceDiGradimento;
    }


    @Override
    public Integer getId() {
        return this.idCommento;
    }

    @Override
    public String getTesto() {
        return this.testo;
    }

    @Override
    public Utente getUtente() {
        return this.cliente;
    }

    @Override
    public Timestamp getDataEOra() {
        return this.dataEOra;
    }

    @Override
    public String toString() {
        return "CommentoCliente{" +
                "idCommento=" + idCommento +
                ", cliente=" + cliente.getId() +
                ", idArticolo=" + idArticolo +
                ", testo='" + testo + '\'' +
                ", indiceDiGradimento=" + indiceDiGradimento +
                ", dataEOra=" + dataEOra +
                '}';
    }
}
