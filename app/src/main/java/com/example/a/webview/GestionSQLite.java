package com.example.a.webview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class GestionSQLite {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "urls.db";

    private static final String TABLE_URL = "table_url";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_URL = "url";
    private static final int NUM_COL_URL= 1;

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public GestionSQLite(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertUrl(String url){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_URL, url);
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_URL, null, values);
    }

     /**
     * Récupère notre liste d'urls
     * @return
     */
    public List getAllUrls()
    {
        List listUrls = new ArrayList();

        Cursor cursor = bdd.query(
                maBaseSQLite.getBddUrl(),    //instance de connexion
                new String[] {"url"}, // clause SELECT
                null,                         // clause WHERE
                null,                         // arguments de la clause WHERE
                null,                         // clause GROUP BY
                null,                         // clause HAVING
                null                          // clause ORDER BY
        );

        // parcours le tableau de curseur en curseur
        // pour ajouter chaque url à notre liste
        while(cursor.moveToNext()){

            String url = cursor.getString(0);
            listUrls.add(url);
        }
        return listUrls;
    }


    public void removeUrlWithId(int id){
        bdd.delete(TABLE_URL, COL_ID +" = "+id, null );
    }

    public int getUrlWithNom(String nom){
        //Récupère dans un Cursor les valeurs correspondant à une item contenue dans la BDD
        Cursor c = bdd.query(TABLE_URL, new String[] {COL_ID, COL_URL}, COL_URL + " LIKE \"" + nom +"\"", null, null, null, null);
        return cursorToItem(c);
    }

    private int cursorToItem(Cursor c){
        //On se place sur le premier élément
        c.moveToFirst();
        //On récupère l'id
        int id = c.getInt(NUM_COL_ID);
        c.close();

        //On retourne l'id
        return id;
    }
}
