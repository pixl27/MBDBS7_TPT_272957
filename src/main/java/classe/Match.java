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
    int id;
    int idTeam1;
    int idTeam2;
    int idMatchRivalry;

    public int getIdMatchRivalry() {
        return idMatchRivalry;
    }
    Date datematch;

    public Match(int idTeam1, int idTeam2, int idMatchRivalry, Date datematch) {
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.idMatchRivalry = idMatchRivalry;
        this.datematch = datematch;
    }

    public void setIdMatchRivalry(int idMatchRivalry) {
        this.idMatchRivalry = idMatchRivalry;
    }
    

   
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Match(int idTeam1, int idTeam2, Date datematch) {
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.datematch = datematch;
    }
}
