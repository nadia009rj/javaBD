/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabd;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrateur
 */
public class JavaBd {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //pour lancer la requette
        testBD();
        afficheEditeurs();
        try {
            //recuperation d un connexion a la Bd
            Connection cn = getDatabaseConnection();
            showSelectQuery("SELECT auteurs.nom, livres.titre FROM livres INNER JOIN auteurs ON auteurs.id = livres.auteur_id", cn);
            deleteAuthor(31);

// application de parametre
            //int c est un chiffre (comment de ligne)
           /* 
             int affectedRows = addAuthor("Joseph", "Conrad");
             System.out.println("la requête concerne " + affectedRows + " lignes");
             */
            String[] noms = {"Ronsard", "Du Bellay", "Belleau", "Neruda"};
            String[] prenoms = {"Pierre", "Joaquim", "Remi", "pablo"};
            addAuthors(prenoms, noms);
            
        } catch (SQLException ex) {
            Logger.getLogger(JavaBd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int deleteAuthor(int id) throws SQLException{
       
    Connection cn = getDatabaseConnection();
     //requete sq paramètrée
        String sql = " DELETE FROM auteurs WHERE id = ? ";

        //creation de l'objet preparedStatement
        PreparedStatement pstm = cn.prepareStatement(sql);

        //passage des valeurs
        pstm.setInt(1, id);
        
       return pstm.executeUpdate();
}
    public static int addAuthor(String prenom, String nom) throws SQLException {

        //reccuperation de la connexion
        Connection cn = getDatabaseConnection();

        //requete sq paramètrée
        String sql = "INSERT INTO auteurs (prenom, nom) VALUE (?,?)";

        //creation de l'objet preparedStatement
        PreparedStatement pstm = cn.prepareStatement(sql);

        //passage des valeurs
        pstm.setString(1, prenom);
        pstm.setString(2, nom);

        //execution de la requete
        return pstm.executeUpdate();
    }

    public static void addAuthors(String[] firstNames, String[] names) throws SQLException {

        //reccuperation de la connexion
        Connection cn = getDatabaseConnection();

        //requete sq paramètrée
        String sql = "INSERT INTO auteurs (prenom, nom) VALUE (?,?)";

        //creation de l'objet preparedStatement
        PreparedStatement pstm = cn.prepareStatement(sql);
        //nombre de noms
        int nbNames = names.length;
        //boucle sur les noms
        for (int i = 0; i < nbNames; i++) {
            //passage des valeurs
            pstm.setString(1, firstNames[i]);
            pstm.setString(2, names[i]);
            //execution de la requete
            pstm.executeUpdate();
        }

    }

    public static Connection getDatabaseConnection() throws SQLException {
        Connection cn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bibliotheque",
                "root",
                ""
        );
        return cn;

    }

    public static void showSelectQuery(String sql, Connection cn) throws SQLException {
        //creation du statement
        Statement stm = cn.createStatement();
        //execution de la requete
        ResultSet rs = stm.executeQuery(sql);

        //obtention du nombre des colonnes de la requete
        //a partir des meta données de l 'objet Resultset
        int columnCount = rs.getMetaData().getColumnCount();

        StringBuilder sb = new StringBuilder();

//boucle par lignes du Resultset
        while (rs.next()) {
            //boucle sur les colonnes
            for (int i = 1; i < columnCount; i++) {
                //affichage de la valeur d'une colonne
                sb.append(rs.getString(i));
                sb.append("\t\t\t\t\t\t");

            }
            //retour à la ligne
            sb.append(System.lineSeparator());
        }
        //affichage du resultat

        System.out.println(sb.toString());

    }

    public static void afficheEditeurs() {
        try {
            Connection cn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/bibliotheque",
                    "root",
                    ""
            );
            //requete SQL à EXECUTER
            String sql = "SELECT nom FROM editeurs";
            //StringBuilder pour afficher le resultat
            StringBuilder sb = new StringBuilder();
            //creation d un objet Statement
            //nécéssaire pour exécuter une requeteexecution de la requete

            Statement stm = cn.createStatement();
            // execution de la requete
            //retourne un objet resulSet
            //qui contient le resultat de la requete Select
            ResultSet rs = stm.executeQuery(sql);
            //boucler sur tout le contenu de resuilset
            //tant que rs.next() retourne true

            String nom;
            while (rs.next()) {
                nom = rs.getString("nom");

                sb.append(rs.getString("nom"));
                //ajout du serapateur de lignes \n ou \r\n selon la plate form
                sb.append(System.lineSeparator());
            }

            System.out.println(sb.toString());

        } catch (SQLException ex) {
            Logger.getLogger(JavaBd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void testBD() {
        //connexion
        try {
            Connection cn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/bibliotheque",
                    "root",
                    ""
            );
            //requete SQL à EXECUTER
            String sql = "SELECT prenom, nom FROM auteurs";
            //StringBuilder pour afficher le resultat
            StringBuilder sb = new StringBuilder();
            //creation d un objet Statement
            //nécéssaire pour exécuter une requeteexecution de la requete

            Statement stm = cn.createStatement();
            // execution de la requete
            //retourne un objet resulSet
            //qui contient le resultat de la requete Select
            ResultSet rs = stm.executeQuery(sql);
            //boucler sur tout le contenu de resuilset
            //tant que rs.next() retourne true
            String prenom;
            while (rs.next()) {
                prenom = rs.getString("prenom");
                if (!prenom.isEmpty()) {
                    sb.append(prenom);
                    sb.append(" ");
                }

                sb.append(rs.getString("nom"));
                //ajout du serapateur de lignes \n ou \r\n selon la plate form
                sb.append(System.lineSeparator());
            }

            // affichage du resultat
            System.out.println(sb.toString());

        } catch (SQLException ex) {
            Logger.getLogger(JavaBd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
