package io.github.quotecc.subtasks;

import java.util.Date;

/**
 * Created by cCorliss on 5/1/17.
 */

public class Task {
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

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
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
    //ID|Content|Due|Note|Parent

    private int id;
    private String content;
    private String due;
    private String note;
    private int parent;

    public Task(){}

    public Task(int id, String content, String due, String note, int parent){
        this.id = id;
        this.content = content;
        this.due = due;
        this.note = note;
        this.parent = parent;

    }
    


}
