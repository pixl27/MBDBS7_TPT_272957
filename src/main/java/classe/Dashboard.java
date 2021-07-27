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
public class Dashboard {
    
    double earnings;
    int nbrParis;
    int pourcentage;

    public Dashboard() {
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getNbrParis() {
        return nbrParis;
    }

    public void setNbrParis(int nbrParis) {
        this.nbrParis = nbrParis;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Dashboard(double earnings, int nbrParis, int pourcentage) {
        this.earnings = earnings;
        this.nbrParis = nbrParis;
        this.pourcentage = pourcentage;
    }
    
}
