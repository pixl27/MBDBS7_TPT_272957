package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MatchDetail {

    @SerializedName("idTeam1")
    private  int idTeam1;
    @SerializedName("idTeam2")
    private  int idTeam2;
    @SerializedName("idMatchRivalry")
    private  int idMatchRivalry;
    @SerializedName("datematch")
    private Date datematch;
    @SerializedName("nomTeam1")
    private  String nomTeam1;
    @SerializedName("nomTeam2")
    private  String nomTeam2;
    @SerializedName("odds1")
    private  Double odds1;
    @SerializedName("odds2")
    private  Double odds2;
    @SerializedName("logo1")
    private  String logo1;
    @SerializedName("logo2")
    private  String logo2;
    @SerializedName("time")
    private  Date time;
    @SerializedName("tournois")
    private  String tournois;
    @SerializedName("nbrMap")
    private  int nbrMap;

    @SerializedName("odd1_map1")
    private  Double odd1_map1;
    @SerializedName("odd1_map2")
    private  Double odd1_map2;
    @SerializedName("odd1_map3")
    private  Double  odd1_map3;
    @SerializedName("odd1_map4")
    private  Double odd1_map4;
    @SerializedName("odd1_map5")
    private  Double odd1_map5;
    @SerializedName("odd1_fb_map1")
    private  Double odd1_fb_map1;
    @SerializedName("odd1_fb_map2")
    private  Double odd1_fb_map2;
    @SerializedName("odd1_fb_map3")
    private  Double odd1_fb_map3;
    @SerializedName("odd1_fb_map4")
    private  Double odd1_fb_map4;
    @SerializedName("odd1_fb_map5")
    private  Double odd1_fb_map5;
    @SerializedName("odd2_map1")
    private  Double odd2_map1;
    @SerializedName("odd2_map2")
    private  Double odd2_map2;
    @SerializedName("odd2_map3")
    private  Double odd2_map3;
    @SerializedName("odd2_map4")
    private  Double odd2_map4;
    @SerializedName("odd2_map5")
    private  Double odd2_map5;
    @SerializedName("odd2_fb_map1")
    private  Double odd2_fb_map1;
    @SerializedName("odd2_fb_map2")
    private  Double odd2_fb_map2;
    @SerializedName("odd2_fb_map3")
    private  Double odd2_fb_map3;
    @SerializedName("odd2_fb_map4")
    private  Double odd2_fb_map4;
    @SerializedName("odd2_fb_map5")
    private  Double odd2_fb_map5;

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

    public Double getOdds1() {
        return odds1;
    }

    public void setOdds1(Double odds1) {
        this.odds1 = odds1;
    }

    public Double getOdds2() {
        return odds2;
    }

    public void setOdds2(Double odds2) {
        this.odds2 = odds2;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTournois() {
        return tournois;
    }

    public void setTournois(String tournois) {
        this.tournois = tournois;
    }

    public int getNbrMap() {
        return nbrMap;
    }

    public void setNbrMap(int nbrMap) {
        this.nbrMap = nbrMap;
    }

    public Double getOdd1_map1() {
        return odd1_map1;
    }

    public void setOdd1_map1(Double odd1_map1) {
        this.odd1_map1 = odd1_map1;
    }

    public Double getOdd1_map2() {
        return odd1_map2;
    }

    public void setOdd1_map2(Double odd1_map2) {
        this.odd1_map2 = odd1_map2;
    }

    public Double getOdd1_map3() {
        return odd1_map3;
    }

    public void setOdd1_map3(Double odd1_map3) {
        this.odd1_map3 = odd1_map3;
    }

    public Double getOdd1_map4() {
        return odd1_map4;
    }

    public void setOdd1_map4(Double odd1_map4) {
        this.odd1_map4 = odd1_map4;
    }

    public Double getOdd1_map5() {
        return odd1_map5;
    }

    public void setOdd1_map5(Double odd1_map5) {
        this.odd1_map5 = odd1_map5;
    }

    public Double getOdd1_fb_map1() {
        return odd1_fb_map1;
    }

    public void setOdd1_fb_map1(Double odd1_fb_map1) {
        this.odd1_fb_map1 = odd1_fb_map1;
    }

    public Double getOdd1_fb_map2() {
        return odd1_fb_map2;
    }

    public void setOdd1_fb_map2(Double odd1_fb_map2) {
        this.odd1_fb_map2 = odd1_fb_map2;
    }

    public Double getOdd1_fb_map3() {
        return odd1_fb_map3;
    }

    public void setOdd1_fb_map3(Double odd1_fb_map3) {
        this.odd1_fb_map3 = odd1_fb_map3;
    }

    public Double getOdd1_fb_map4() {
        return odd1_fb_map4;
    }

    public void setOdd1_fb_map4(Double odd1_fb_map4) {
        this.odd1_fb_map4 = odd1_fb_map4;
    }

    public Double getOdd1_fb_map5() {
        return odd1_fb_map5;
    }

    public void setOdd1_fb_map5(Double odd1_fb_map5) {
        this.odd1_fb_map5 = odd1_fb_map5;
    }

    public Double getOdd2_map1() {
        return odd2_map1;
    }

    public void setOdd2_map1(Double odd2_map1) {
        this.odd2_map1 = odd2_map1;
    }

    public Double getOdd2_map2() {
        return odd2_map2;
    }

    public void setOdd2_map2(Double odd2_map2) {
        this.odd2_map2 = odd2_map2;
    }

    public Double getOdd2_map3() {
        return odd2_map3;
    }

    public void setOdd2_map3(Double odd2_map3) {
        this.odd2_map3 = odd2_map3;
    }

    public Double getOdd2_map4() {
        return odd2_map4;
    }

    public void setOdd2_map4(Double odd2_map4) {
        this.odd2_map4 = odd2_map4;
    }

    public Double getOdd2_map5() {
        return odd2_map5;
    }

    public void setOdd2_map5(Double odd2_map5) {
        this.odd2_map5 = odd2_map5;
    }

    public Double getOdd2_fb_map1() {
        return odd2_fb_map1;
    }

    public void setOdd2_fb_map1(Double odd2_fb_map1) {
        this.odd2_fb_map1 = odd2_fb_map1;
    }

    public Double getOdd2_fb_map2() {
        return odd2_fb_map2;
    }

    public void setOdd2_fb_map2(Double odd2_fb_map2) {
        this.odd2_fb_map2 = odd2_fb_map2;
    }

    public Double getOdd2_fb_map3() {
        return odd2_fb_map3;
    }

    public void setOdd2_fb_map3(Double odd2_fb_map3) {
        this.odd2_fb_map3 = odd2_fb_map3;
    }

    public Double getOdd2_fb_map4() {
        return odd2_fb_map4;
    }

    public void setOdd2_fb_map4(Double odd2_fb_map4) {
        this.odd2_fb_map4 = odd2_fb_map4;
    }

    public Double getOdd2_fb_map5() {
        return odd2_fb_map5;
    }

    public void setOdd2_fb_map5(Double odd2_fb_map5) {
        this.odd2_fb_map5 = odd2_fb_map5;
    }
}
