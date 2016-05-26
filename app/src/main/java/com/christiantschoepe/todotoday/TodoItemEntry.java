package com.christiantschoepe.todotoday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by user on 5/12/2016.
 */
public abstract class TodoItemEntry implements BaseColumns {

    /* Inner class that defines the table contents */
    public static final String TABLE_NAME = "todo_item";
    public static final String COLUMN_NAME_ITEM_ID = "item_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_ARCHIVED = "archived";
    public static final String COLUMN_NAME_DUE_AT = "due_at";
}
