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
public class GraphJourSemaine {
    int lundi;
    int mardi;
    int mercredi;
    int jeudi;
    int vendredi;
    int samedi;

    public GraphJourSemaine() {
    }
    int dimanche;

    public int getLundi() {
        return lundi;
    }

    public void setLundi(int lundi) {
        this.lundi = lundi;
    }

    public int getMardi() {
        return mardi;
    }

    public void setMardi(int mardi) {
        this.mardi = mardi;
    }

    public int getMercredi() {
        return mercredi;
    }

    public void setMercredi(int mercredi) {
        this.mercredi = mercredi;
    }

    public int getJeudi() {
        return jeudi;
    }

    public void setJeudi(int jeudi) {
        this.jeudi = jeudi;
    }

    public int getVendredi() {
        return vendredi;
    }

    public void setVendredi(int vendredi) {
        this.vendredi = vendredi;
    }

    public int getSamedi() {
        return samedi;
    }

    public void setSamedi(int samedi) {
        this.samedi = samedi;
    }

    public int getDimanche() {
        return dimanche;
    }

    public void setDimanche(int dimanche) {
        this.dimanche = dimanche;
    }

    public GraphJourSemaine(int lundi, int mardi, int mercredi, int jeudi, int vendredi, int samedi, int dimanche) {
        this.lundi = lundi;
        this.mardi = mardi;
        this.mercredi = mercredi;
        this.jeudi = jeudi;
        this.vendredi = vendredi;
        this.samedi = samedi;
        this.dimanche = dimanche;
    }
    
    
}
