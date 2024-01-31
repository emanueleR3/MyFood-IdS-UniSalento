package it.unisalento.myfood.model.Composite;

import java.util.ArrayList;

public interface IArticolo {

    String getNome();

    Float getPrezzo();

    ArrayList<CommentoCliente> getCommenti();

    void setCommenti(ArrayList<CommentoCliente> commenti);

    Integer getId();

    void setId(Integer id);

    String getDescrizione();

    ArrayList<String> getDirectoryFoto();

    void setDirectoryFoto(ArrayList<String> directoryFoto);

    Integer getPezziDisponibili();
}
