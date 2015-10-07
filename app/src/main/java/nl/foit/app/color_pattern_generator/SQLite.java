package nl.foit.app.color_pattern_generator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Frits on 16-8-2015.
 */
public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context context) {
        super(context, "database.db", null, 1);
    }


    public String color_favorites_table = "color_combo";
    public String color_1 = "color_1";
    public String color_name = "color_name";
    public String color_date = "color_date";
    public String id = "_id";
    public int wanted_colors = 32;

    public void open() {
        this.getWritableDatabase();
    }

    public void close() {
        this.getWritableDatabase().close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + color_favorites_table +
                "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                color_name + " TEXT NOT NULL," +
                color_date + " TEXT NOT NULL," +
                color_1 + " TEXT NOT NULL," +
                constructQuery() +
                ")");
    }

    public String constructQuery() {
        StringBuilder s = new StringBuilder();
        for (int i = 2; i < 31; i++) {
            s.append("color_" + i + ",");
        }
        String foo = s.toString().substring(0, s.toString().length() - 1);
        return foo;
    }

    public void remove_row(String row) {
        getWritableDatabase().execSQL("Delete FROM " + color_favorites_table + " WHERE _id='" + row + "';");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + color_favorites_table);
        onCreate(db);
    }

    public Cursor preferenceData() {

        Cursor c = getWritableDatabase().query(color_favorites_table, getWhere(), null, null, null, null, "_id DESC", null);

        if (c != null) {
            c.moveToFirst();

        }
//        Log.e("what cursor did", c.getString(c.getColumnIndex("_id")));
        return c;
    }

    public String[] getWhere() {
        String[] foo = new String[wanted_colors];
        foo[0] = id;
        foo[1] = color_name;
        foo[2] = color_date;
        int d = 3;
        int f = 1;
        while (d < foo.length)
            foo[d++] = "color_" + f++;
        return foo;
    }


}
