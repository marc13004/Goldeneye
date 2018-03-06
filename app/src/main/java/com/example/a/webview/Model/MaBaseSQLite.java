package com.example.a.webview.Model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class MaBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_URL = "table_url";
    private static final String COL_ID = "ID";
    private static final String COL_URL = "url";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_URL + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_URL + " TEXT NOT NULL);";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public String getBddUrl()
    {
        return TABLE_URL;
    }

    /**
     * Création de la base SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_URL + ";");
        onCreate(db);
    }
}
