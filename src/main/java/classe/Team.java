/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

/**
 *
 * @author tolot
 */
public class Team {
    int idTeam;
    String nom;
    String tag;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    String logo;

    public Team(){}
    public Team(int idTeam, String logo) {
        this.idTeam = idTeam;
        this.logo = logo;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Team(int idTeam, String nom, String tag) {
        this.idTeam = idTeam;
        this.nom = nom;
        this.tag = tag;
    }
    
    
    
}
