package com.christiantschoepe.todotoday;

import java.sql.Time;
import java.util.Date;

/**
 * Created by user on 5/12/2016.
 */
public class TodoItem {

    public int id;
    public String title = "";
    public String description = "";
    public int archived;
    public Date dueAt;

    public TodoItem(int id, String title, String desc, Date dueAt) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.archived = 0;
        this.dueAt = dueAt;
    }

    public void archive() {
        this.archived = 1;
    }

}
