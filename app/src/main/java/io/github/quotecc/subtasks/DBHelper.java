package io.github.quotecc.subtasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cCorliss on 5/2/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_DUE = "due";
    public static final String COLUMN_NAME_NOTE = "note";
    public static final String COLUMN_NAME_PARENT = "parent";

    public static final String DATABASE_NAME = "subtasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "+ TABLE_NAME +
            "( "+ COLUMN_NAME_ID + " integer not null, " +
            COLUMN_NAME_CONTENT + " text not null, "+
            COLUMN_NAME_DUE + " text, " +
            COLUMN_NAME_NOTE + " text, " +
            COLUMN_NAME_PARENT + " integer not null" + ");";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers){
        Log.w(DBHelper.class.getName(), "Upgrading from vers "+ oldVers+ " to vers " + newVers);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
