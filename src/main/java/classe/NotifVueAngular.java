/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author tolot
 */
public class NotifVueAngular {

    public NotifVueAngular() {
    }
    
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String idUser;
    String titre;
    String description;
    int vue;
    Date dateNotif;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVue() {
        return vue;
    }

    public void setVue(int vue) {
        this.vue = vue;
    }

    public Date getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(Date dateNotif) {
        this.dateNotif = dateNotif;
    }

    public NotifVueAngular(int id,String idUser, String titre, String description, int vue, Date dateNotif) {
        this.id = id;
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.vue = vue;
        this.dateNotif = dateNotif;
    }
    
    public static void insererNotif(String idUser,String titre,String description) throws SQLException{
        
            OracleConnection connection = Connexion.getConnection();
            Statement statement = null;
            try{
                int vue = 0;
                Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                statement = connection.createStatement();
           
                statement.executeQuery("insert into NOTIFVUEANGULAR values(SEQNOTIF.NEXTVAL,'"+idUser+"','"+titre+"','"+description+"',"+vue+",TO_DATE('"+datenow+"','YYYY-MM-DD')) ");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
                connection.close();
            }
       }
    
}
