/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Date;

/**
 *
 * @author tolot
 */
public class Paris {
    
     int idParis;
    int idUser;
    int idMatch;
    int idTeam;
    String type;
    float montant;
    float odds;
    Date dateparis;
    int statut;

    public int getIdParis() {
        return idParis;
    }

    public void setIdParis(int idParis) {
        this.idParis = idParis;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public float getOdds() {
        return odds;
    }

    public void setOdds(float odds) {
        this.odds = odds;
    }

    public Date getDateparis() {
        return dateparis;
    }

    public void setDateparis(Date dateparis) {
        this.dateparis = dateparis;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
    
   

    public Paris(int idParis, int idUser, int idMatch, int idTeam, String type, float montant, float odds, Date dateparis, int statut) {
        this.idParis = idParis;
        this.idUser = idUser;
        this.idMatch = idMatch;
        this.idTeam = idTeam;
        this.type = type;
        this.montant = montant;
        this.odds = odds;
        this.dateparis = dateparis;
        this.statut = statut;
    }
    
    
    
    
    
}
