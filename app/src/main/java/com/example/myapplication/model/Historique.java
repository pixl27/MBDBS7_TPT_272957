package com.example.myapplication.model;

import java.util.Date;

public class Historique {


    private String iduser;
    private Double montant;
    private String type;
    private int idparis;
    private String description;
    private Date datehistorique;

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdparis() {
        return idparis;
    }

    public void setIdparis(int idparis) {
        this.idparis = idparis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatehistorique() {
        return datehistorique;
    }

    public void setDatehistorique(Date datehistorique) {
        this.datehistorique = datehistorique;
    }
}
