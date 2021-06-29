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
    String logo1;
    String logo2;
    
    
    //odd1 map
    float odd1_map1;
    float odd1_map2;
    float odd1_map3;
    float odd1_map4;
    float odd1_map5;
    
     //odd1 fb
    float odd1_fb_map1;
    float odd1_fb_map2;
    float odd1_fb_map3;
    float odd1_fb_map4;
    float odd1_fb_map5;
    
    
    
    //odd2 map
    float odd2_map1;
    float odd2_map2;
    float odd2_map3;
    float odd2_map4;
    float odd2_map5;
    
     //odd2 fb
    float odd2_fb_map1;
    float odd2_fb_map2;
    float odd2_fb_map3;
    float odd2_fb_map4;
    float odd2_fb_map5;
    
    public MatchAPI(){
        
    }

    public float getOdd1_map1() {
        return odd1_map1;
    }

    public void setOdd1_map1(float odd1_map1) {
        this.odd1_map1 = odd1_map1;
    }

    public float getOdd1_map2() {
        return odd1_map2;
    }

    public void setOdd1_map2(float odd1_map2) {
        this.odd1_map2 = odd1_map2;
    }

    public float getOdd1_map3() {
        return odd1_map3;
    }

    public void setOdd1_map3(float odd1_map3) {
        this.odd1_map3 = odd1_map3;
    }

    public float getOdd1_map4() {
        return odd1_map4;
    }

    public void setOdd1_map4(float odd1_map4) {
        this.odd1_map4 = odd1_map4;
    }

    public float getOdd1_map5() {
        return odd1_map5;
    }

    public void setOdd1_map5(float odd1_map5) {
        this.odd1_map5 = odd1_map5;
    }

    public float getOdd1_fb_map1() {
        return odd1_fb_map1;
    }

    public void setOdd1_fb_map1(float odd1_fb_map1) {
        this.odd1_fb_map1 = odd1_fb_map1;
    }

    public float getOdd1_fb_map2() {
        return odd1_fb_map2;
    }

    public void setOdd1_fb_map2(float odd1_fb_map2) {
        this.odd1_fb_map2 = odd1_fb_map2;
    }

    public float getOdd1_fb_map3() {
        return odd1_fb_map3;
    }

    public void setOdd1_fb_map3(float odd1_fb_map3) {
        this.odd1_fb_map3 = odd1_fb_map3;
    }

    public float getOdd1_fb_map4() {
        return odd1_fb_map4;
    }

    public void setOdd1_fb_map4(float odd1_fb_map4) {
        this.odd1_fb_map4 = odd1_fb_map4;
    }

    public float getOdd1_fb_map5() {
        return odd1_fb_map5;
    }

    public void setOdd1_fb_map5(float odd1_fb_map5) {
        this.odd1_fb_map5 = odd1_fb_map5;
    }

    public float getOdd2_map1() {
        return odd2_map1;
    }

    public void setOdd2_map1(float odd2_map1) {
        this.odd2_map1 = odd2_map1;
    }

    public float getOdd2_map2() {
        return odd2_map2;
    }

    public void setOdd2_map2(float odd2_map2) {
        this.odd2_map2 = odd2_map2;
    }

    public float getOdd2_map3() {
        return odd2_map3;
    }

    public void setOdd2_map3(float odd2_map3) {
        this.odd2_map3 = odd2_map3;
    }

    public float getOdd2_map4() {
        return odd2_map4;
    }

    public void setOdd2_map4(float odd2_map4) {
        this.odd2_map4 = odd2_map4;
    }

    public float getOdd2_map5() {
        return odd2_map5;
    }

    public void setOdd2_map5(float odd2_map5) {
        this.odd2_map5 = odd2_map5;
    }

    public float getOdd2_fb_map1() {
        return odd2_fb_map1;
    }

    public void setOdd2_fb_map1(float odd2_fb_map1) {
        this.odd2_fb_map1 = odd2_fb_map1;
    }

    public float getOdd2_fb_map2() {
        return odd2_fb_map2;
    }

    public void setOdd2_fb_map2(float odd2_fb_map2) {
        this.odd2_fb_map2 = odd2_fb_map2;
    }

    public float getOdd2_fb_map3() {
        return odd2_fb_map3;
    }

    public void setOdd2_fb_map3(float odd2_fb_map3) {
        this.odd2_fb_map3 = odd2_fb_map3;
    }

    public float getOdd2_fb_map4() {
        return odd2_fb_map4;
    }

    public void setOdd2_fb_map4(float odd2_fb_map4) {
        this.odd2_fb_map4 = odd2_fb_map4;
    }

    public float getOdd2_fb_map5() {
        return odd2_fb_map5;
    }

    public void setOdd2_fb_map5(float odd2_fb_map5) {
        this.odd2_fb_map5 = odd2_fb_map5;
    }

    public String getLogo1() {
        return logo1;
    }

    public void setLogo1(String logo1) {
        this.logo1 = logo1;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }
    String time;
    String tournois;
    int nbrMap;

    public int getNbrMap() {
        return nbrMap;
    }

    public void setNbrMap(int nbrMap) {
        this.nbrMap = nbrMap;
    }

    public MatchAPI(int idTeam1, int idTeam2, int idMatchRivalry, Date datematch, String nomTeam1, String nomTeam2, float odds1, float odds2, String logo1, String logo2, String time, String tournois, int nbrMap) {
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.idMatchRivalry = idMatchRivalry;
        this.datematch = datematch;
        this.nomTeam1 = nomTeam1;
        this.nomTeam2 = nomTeam2;
        this.odds1 = odds1;
        this.odds2 = odds2;
        this.logo1 = logo1;
        this.logo2 = logo2;
        this.time = time;
        this.tournois = tournois;
        this.nbrMap = nbrMap;
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

    
    
    
}
