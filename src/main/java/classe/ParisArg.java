/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author tolot
 */
public class ParisArg {

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdTeamParier() {
        return idTeamParier;
    }

    public void setIdTeamParier(int idTeamParier) {
        this.idTeamParier = idTeamParier;
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
    int idUser;
    int idMatch;
    String type;
    int idTeamParier;
   float montant;
   float odds;

    public ParisArg(int idUser, int idMatch, String type, int idTeamParier, float montant, float odds) {
        this.idUser = idUser;
        this.idMatch = idMatch;
        this.type = type;
        this.idTeamParier = idTeamParier;
        this.montant = montant;
        this.odds = odds;
    }
    
    
   
   
}
