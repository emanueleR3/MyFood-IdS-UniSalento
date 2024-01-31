package it.unisalento.myfood.model.Composite;

import java.util.ArrayList;

public class Menu implements IArticolo {

    private Integer id;

    private String nome;

    private String descrizione;

    private Integer pezziDisponibili;

    private ArrayList<IArticolo> articoli = new ArrayList<>();

    private Float sconto = 0f; //valore 0..1

    private ArrayList<CommentoCliente> commenti;
    private ArrayList<String> directoryFoto;

    public Menu() {
    }

    public Menu(String nome, String descrizione, Integer pezziDisponibili, ArrayList<IArticolo> articoli, Float sconto, ArrayList<CommentoCliente> commenti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.pezziDisponibili = pezziDisponibili;
        this.articoli = articoli;
        this.sconto = sconto;
        this.commenti = commenti;
    }

    //TODO: lo sconto presente sulla traccia è opportuno considerarlo come una questione di view, modificabile dall'amministratore dall'interfaccia grafica

    // TODO: va nel business secondo me un controllo del genere --> nell'AbstractFactory, non nel model
    public void add(IArticolo articolo) {
        if(articolo instanceof Prodotto) {
            articoli.add(articolo);
            // TODO: controllo su cosa stai aggiungendo
            return;
        } else {
            Menu menu = (Menu) articolo;
            for(IArticolo a : menu.getArticoli()) {
                if(a instanceof Menu) {
                    return;
                }
            }
            articoli.add(articolo);
        }
    }



    public ArrayList<IArticolo> getArticoli() {
        return articoli;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public Float getPrezzo() {
        Float somma = 0f;
        for (IArticolo a : articoli){
            somma += a.getPrezzo();
        }

        // Calcola il prezzo scontato
        float prezzoScontato = somma * (1 - sconto);

        // Arrotonda alla seconda cifra decimale
        prezzoScontato = (float) (Math.round(prezzoScontato * 100.0) / 100.0);

        return prezzoScontato;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getDescrizione() {
        return this.descrizione;
    }

    @Override
    public ArrayList<String> getDirectoryFoto() {
        return directoryFoto;
    }

    @Override
    public void setDirectoryFoto(ArrayList<String> directoryFoto) {
        this.directoryFoto = directoryFoto;
    }

    @Override
    public Integer getPezziDisponibili() {
        return this.pezziDisponibili;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getSconto() {
        return sconto;
    }

    public void setSconto(Float sconto) {
        this.sconto = sconto;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setPezziDisponibili(Integer pezziDisponibili) {
        this.pezziDisponibili = pezziDisponibili;
    }

    public void setArticoli(ArrayList<IArticolo> articoli) {
        this.articoli = articoli;
    }

    @Override
    public void setCommenti(ArrayList<CommentoCliente> commenti) {
        this.commenti = commenti;
    }

    @Override
    public ArrayList<CommentoCliente> getCommenti() {
        return commenti;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", pezziDisponibili=" + pezziDisponibili +
                ", articoli=" + articoli +
                ", sconto=" + sconto +
                ", commenti=" + commenti +
                '}';
    }
}
