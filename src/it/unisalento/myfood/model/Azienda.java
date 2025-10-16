package it.unisalento.myfood.model;

import java.util.ArrayList;

public class Azienda {

    private Integer id;
    private String nome;
    private String partitaIVA;

    private ArrayList<Integer> idIngredientiProdotti;

    private ArrayList<Integer> idIngredientiDistribuiti;

    public Azienda() {
    }

    public Azienda(String nome, String partitaIVA, ArrayList<Integer> idIngredientiProdotti, ArrayList<Integer> idIngredientiDistribuiti) {
        this.nome = nome;
        this.partitaIVA = partitaIVA;
        this.idIngredientiProdotti = idIngredientiProdotti;
        this.idIngredientiDistribuiti = idIngredientiDistribuiti;
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

    public String getPartitaIVA() {
        return partitaIVA;
    }

    public void setPartitaIVA(String partitaIVA) {
        this.partitaIVA = partitaIVA;
    }

    public ArrayList<Integer> getIdIngredientiProdotti() {
        return idIngredientiProdotti;
    }

    public void setIdIngredientiProdotti(ArrayList<Integer> idIngredientiProdotti) {
        this.idIngredientiProdotti = idIngredientiProdotti;
    }

    public ArrayList<Integer> getIdIngredientiDistribuiti() {
        return idIngredientiDistribuiti;
    }

    public void setIdIngredientiDistribuiti(ArrayList<Integer> idIngredientiDistribuiti) {
        this.idIngredientiDistribuiti = idIngredientiDistribuiti;
    }

    @Override
    public String toString() {
        return "Azienda{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", partitaIVA='" + partitaIVA + '\'' +
                ", idIngredientiProdotti=" + idIngredientiProdotti +
                ", idIngredientiDistribuiti=" + idIngredientiDistribuiti +
                '}';
    }
}
