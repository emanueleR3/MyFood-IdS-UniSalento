package it.unisalento.myfood.model;

import java.awt.*;

public class Immagine {

    private Integer idArticolo;
    private String path;

    private Image immagine;

    public Integer getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    public Image getImmagine() {
        return immagine;
    }

    public void setImmagine(Image immagine) {
        this.immagine = immagine;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
