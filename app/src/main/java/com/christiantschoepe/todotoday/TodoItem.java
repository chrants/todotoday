package com.christiantschoepe.todotoday;

/**
 * Created by user on 5/12/2016.
 */
public class TodoItem {

    public int id;
    public String title = "";
    public String description = "";
    public int archived;

    public TodoItem(int id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.archived = 0;
    }

    public void archive() {
        this.archived = 1;
    }

}
