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
public class Match {

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

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

    public Date getDatematch() {
        return datematch;
    }

    public void setDatematch(Date datematch) {
        this.datematch = datematch;
    }

    public int getNbrMap() {
        return nbrMap;
    }

    public void setNbrMap(int nbrMap) {
        this.nbrMap = nbrMap;
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

    public Match() {
    }
    
   int idMatch;
   int idTeam1;
   int idTeam2;
   Date datematch;
   int nbrMap;
   String nomTeam1;
   String nomTeam2;

    public Match(int idMatch, int idTeam1, int idTeam2, Date datematch, int nbrMap, String nomTeam1, String nomTeam2) {
        this.idMatch = idMatch;
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.datematch = datematch;
        this.nbrMap = nbrMap;
        this.nomTeam1 = nomTeam1;
        this.nomTeam2 = nomTeam2;
    }
   
   
}
