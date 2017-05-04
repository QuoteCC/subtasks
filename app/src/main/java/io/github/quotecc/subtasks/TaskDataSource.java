package io.github.quotecc.subtasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cCorliss on 5/3/17.
 */

public class TaskDataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private String[] allColumns = {DBHelper.COLUMN_NAME_ID, DBHelper.COLUMN_NAME_CONTENT, DBHelper.COLUMN_NAME_DUE, DBHelper.COLUMN_NAME_NOTE, DBHelper.COLUMN_NAME_PARENT};

    //ID|Content|Due|Note|Parent

    public TaskDataSource(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public List<Task> getAllTasks(){
        List<Task> tasks = new ArrayList<Task>();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Task t = cursorToTask(cursor);
            tasks.add(t);
            cursor.moveToNext();
        }
        cursor.close();
        Collections.sort(tasks);
        return tasks;
    }

    public List<Task> getSubTasks(Task t){
        List<Task> tasks = new ArrayList<Task>();
        String[] args = {t.getId() + ""};
        Cursor cursor = db.query(DBHelper.TABLE_NAME, allColumns, DBHelper.COLUMN_NAME_PARENT + " = ?", args, null, null, null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Task task = cursorToTask(cursor);
                tasks.add(task);
                cursor.moveToNext();
            }
        }
        return tasks;

    }

    public int updateTask(Task t){ //does not touch parent or id, as these will not be changed by any updates
        ContentValues c = new ContentValues();
        c.put(DBHelper.COLUMN_NAME_CONTENT,t.getContent());
        c.put(DBHelper.COLUMN_NAME_DUE, t.getDueS());
        c.put(DBHelper.COLUMN_NAME_NOTE, t.getNote());
        String[] s = {t.getId() + ""};
        int effect = db.update(DBHelper.TABLE_NAME,c,DBHelper.COLUMN_NAME_ID + " = ?", s);
        Log.d("Effect", effect + " Rows effected");
        return effect;


    }

    public void insertTask(Task t){
        ContentValues c = new ContentValues();
        c.put(DBHelper.COLUMN_NAME_ID, ""+t.getId());
        c.put(DBHelper.COLUMN_NAME_CONTENT,t.getContent());
        c.put(DBHelper.COLUMN_NAME_DUE, t.getDueS());
        c.put(DBHelper.COLUMN_NAME_NOTE, t.getNote());
        c.put(DBHelper.COLUMN_NAME_PARENT, "" + t.getParent());

        long insertId = db.insert(DBHelper.TABLE_NAME,null,c);

    }




    private Task cursorToTask(Cursor cursor){
        Task t = new Task();
        t.setId(Integer.parseInt(cursor.getString(0)));
        t.setContent(cursor.getString(1));
        try {
            t.setDue(cursor.getString(2));
        }
        catch (ParseException p){
            Log.d("parse", "PARSE ERROR: " + cursor.getString(2));
        }
        t.setNote(cursor.getString(3));
        t.setParent(Integer.parseInt(cursor.getString(4)));

        return t;
    }


}
