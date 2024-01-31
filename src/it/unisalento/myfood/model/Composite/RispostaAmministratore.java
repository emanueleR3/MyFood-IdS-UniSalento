package it.unisalento.myfood.model.Composite;

import it.unisalento.myfood.model.Utente;

import java.sql.Timestamp;

public class RispostaAmministratore implements IInterazioneUtente {

    private Integer idRiposta;
    private Integer idCommentoRisposto;  //TODO: deve essere IInterazioneUtente per implementare il composite
    private String testo;

    private Utente amministratore;

    private Timestamp dataEOra;

    public RispostaAmministratore() {}

    public RispostaAmministratore(Integer idCommentoRisposto, String testo, Utente amministratore, Timestamp dataEOra) {
        this.idCommentoRisposto = idCommentoRisposto;
        this.testo = testo;
        this.amministratore = amministratore;
        this.dataEOra = dataEOra;
    }

    public void setIdRiposta(Integer idRiposta) {
        this.idRiposta = idRiposta;
    }

    public void setIdCommentoRisposto(Integer idCommentoRisposto) {
        this.idCommentoRisposto = idCommentoRisposto;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setAmministratore(Utente amministratore) {
        this.amministratore = amministratore;
    }

    public void setDataEOra(Timestamp dataEOra) {
        this.dataEOra = dataEOra;
    }

    public Integer getIdCommentoRisposto() {
        return idCommentoRisposto;
    }

    @Override
    public Integer getId() {
        return this.idRiposta;
    }

    @Override
    public String getTesto() {
        return this.testo;
    }

    @Override
    public Utente getUtente() {
        return this.amministratore;
    }

    @Override
    public Timestamp getDataEOra() {
        return this.dataEOra;
    }

    @Override
    public String toString() {
        return "RispostaAmministratore{" +
                "idRiposta=" + idRiposta +
                ", idCommentoRisposto=" + idCommentoRisposto +
                ", testo='" + testo + '\'' +
                ", amministratore=" + amministratore +
                ", dataEOra=" + dataEOra +
                '}';
    }
}
