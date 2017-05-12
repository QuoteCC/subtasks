package io.github.quotecc.subtasks;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cCorliss on 5/1/17.
 */

public class Task implements Comparable<Task> {

    //ID|Content|Due|Note|Parent

    private int id = 0;
    private String content;
    private Date due;
    private String note;
    private int parent;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm a");

    public Task(){}
    public Task(String content, int id, int parent){ // a method for creating a task that simply has words in it, no actual content
        this.id = id;
        due = new Date();
        this.content = content;
        parent = parent;
    }

    public Task(int id, String content, String due, String note, int parent){
        this.id = id;
        this.content = content;
        try {
            this.due = simpleDateFormat.parse(due);
        }
        catch (ParseException p){
            Log.d("parse", "FAILED TO PARSE DATE: " + due);
        }
        this.note = note;
        this.parent = parent;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDue() {
        return due;
    }
    public String getDueS(){ //this allows for storing and formatting dates off consistent strings
        return simpleDateFormat.format(due.getTime());
    }

    public void setDue(String due) throws ParseException {
        this.due = simpleDateFormat.parse(due);
    }
    public void setDue(Date due){ //overloaded method to allow for multiple methods of setting
        this.due = due;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Task t){ //for use in sorting all tasks into by date form

        return due.compareTo(t.getDue());

    }
    @Override
    public String toString(){
        return getContent() +"    " + getId();
    }







}
