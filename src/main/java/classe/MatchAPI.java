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
public class MatchAPI {
    int idTeam1;
    int idTeam2;
    int idMatchRivalry;
    Date datematch;
    String nomTeam1;
    String nomTeam2;
    float odds1;
    float odds2;
    String logo;
    String time;
    String tournois;

    public int getIdTeam1() {
        return idTeam1;
    }

    public void setIdTeam1(int idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public int getIdTeam2() {
        return idTeam2;
    }

    public void setIdTeam2(int idTeam2) {
        this.idTeam2 = idTeam2;
    }

    public int getIdMatchRivalry() {
        return idMatchRivalry;
    }

    public void setIdMatchRivalry(int idMatchRivalry) {
        this.idMatchRivalry = idMatchRivalry;
    }

    public Date getDatematch() {
        return datematch;
    }

    public void setDatematch(Date datematch) {
        this.datematch = datematch;
    }

    public String getNomTeam1() {
        return nomTeam1;
    }

    public void setNomTeam1(String nomTeam1) {
        this.nomTeam1 = nomTeam1;
    }

    public String getNomTeam2() {
        return nomTeam2;
    }

    public void setNomTeam2(String nomTeam2) {
        this.nomTeam2 = nomTeam2;
    }

    public float getOdds1() {
        return odds1;
    }

    public void setOdds1(float odds1) {
        this.odds1 = odds1;
    }

    public float getOdds2() {
        return odds2;
    }

    public void setOdds2(float odds2) {
        this.odds2 = odds2;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTournois() {
        return tournois;
    }

    public void setTournois(String tournois) {
        this.tournois = tournois;
    }

    public MatchAPI(int idTeam1, int idTeam2, int idMatchRivalry, Date datematch, String nomTeam1, String nomTeam2, float odds1, float odds2, String logo, String time, String tournois) {
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.idMatchRivalry = idMatchRivalry;
        this.datematch = datematch;
        this.nomTeam1 = nomTeam1;
        this.nomTeam2 = nomTeam2;
        this.odds1 = odds1;
        this.odds2 = odds2;
        this.logo = logo;
        this.time = time;
        this.tournois = tournois;
    }
    
    
}
