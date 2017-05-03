package io.github.quotecc.subtasks;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cCorliss on 5/3/17.
 */

public class TaskDataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private String[] allColumns = {DBHelper.COLUMN_NAME_ID, DBHelper.COLUMN_NAME_CONTENT, DBHelper.COLUMN_NAME_DUE, DBHelper.COLUMN_NAME_NOTE, DBHelper.COLUMN_NAME_PARENT};

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
        return tasks;
    }



    private Task cursorToTask(Cursor cursor){
        Task t = new Task();
        t.setId(Integer.parseInt(cursor.getString(0)));
        t.setContent(cursor.getString(1));
        return t;
    }


}
