package it.unisalento.myfood.model;

import it.unisalento.myfood.model.Composite.IInterazioneUtente;

import java.util.ArrayList;
import java.sql.Date;

public class Utente {

    public enum RUOLO{
        GUEST,
        CLIENTE,
        CUCINA,
        AMMINISTRATORE
    }

    //pattern di cui mi sfugge il nome
    private Integer id;

    private String nome;
    private String cognome;
    private String email;
    private String hashedPassword;
    private String saltHex;    // Per l'hashing SHA-512
    private String telefono;

    private RUOLO ruolo;

    private Date dataNascita;
    private Date dataRegistrazione;

    private boolean cambiaPassword;
    private boolean disabilitato; //solo se cliente

    private String residenza; //solo se cliente
    private String professione;     //si potrebbe inserire un'entità nel database e creare una classe professione
                                    // molto interessante (cross-selling)
    private ArrayList<Ordine> ordini;//(chiesto da studente) da memorizzare nel database in modo da consigliare all'utente lo stesso ordine fatto in precedenza

    // TODO: caricare in UtenteDAO i commenti
    private ArrayList<IInterazioneUtente> interazioni;

    public Utente() {
    }

    public Utente(String nome, String cognome, String email, String hashedPassword, RUOLO ruolo, Date dataNascita, String telefono, Date dataRegistrazione, boolean cambiaPassword, boolean disabilitato, String residenza, String professione, ArrayList<Ordine> ordini, ArrayList<IInterazioneUtente> interazioni, String saltHex) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.telefono = telefono;
        this.ruolo = ruolo;
        this.dataNascita = dataNascita;
        this.dataRegistrazione = dataRegistrazione;
        this.cambiaPassword = cambiaPassword;
        this.disabilitato = disabilitato;
        this.residenza = residenza;
        this.professione = professione;
        this.ordini = ordini;
        this.interazioni = interazioni;
        this.saltHex = saltHex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RUOLO getRuolo() {
        return ruolo;
    }

    public void setRuolo(RUOLO ruolo) {
        this.ruolo = ruolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getProfessione() {
        return professione;
    }

    public void setProfessione(String professione) {
        this.professione = professione;
    }

    public ArrayList<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(ArrayList<Ordine> ordini) {
        this.ordini = ordini;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public boolean isCambiaPassword() {
        return cambiaPassword;
    }

    public boolean isDisabilitato() {
        return disabilitato;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public void setCambiaPassword(boolean cambiaPassword) {
        this.cambiaPassword = cambiaPassword;
    }

    public void setDisabilitato(boolean disabilitato) {
        this.disabilitato = disabilitato;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    public ArrayList<IInterazioneUtente> getInterazioni() {
        return interazioni;
    }

    public void setInterazioni(ArrayList<IInterazioneUtente> interazioni) {
        this.interazioni = interazioni;
    }

    public String getSaltHex() {
        return saltHex;
    }

    public void setSaltHex(String saltHex) {
        this.saltHex = saltHex;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + hashedPassword + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ruolo=" + ruolo +
                ", dataNascita=" + dataNascita +
                ", dataRegistrazione=" + dataRegistrazione +
                ", cambiaPassword=" + cambiaPassword +
                ", disabilitato=" + disabilitato +
                ", residenza='" + residenza + '\'' +
                ", professione='" + professione + '\'' +
                ", ordini=" + ordini +
                ", interazioni=" + interazioni +
                '}';
    }
}
