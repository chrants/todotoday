package com.christiantschoepe.todotoday;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static com.christiantschoepe.todotoday.TodoItemActivity.*;

public class TodoListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    static final int ADD_TODO_ITEM_REQUEST = 1;
    static final int MODIFY_TODO_ITEM_REQUEST = 2;

    TodoItemDbHelper mDbHelper;
//    public String todolist = "";
    public ArrayList<TodoItem> todolist = new ArrayList<TodoItem>();

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] { TodoItemEntry._ID,
            TodoItemEntry.COLUMN_NAME_TITLE, TodoItemEntry.COLUMN_NAME_DESCRIPTION};

    // This is the select criteria
    static final String SELECTION = "*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbHelper = new TodoItemDbHelper(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(TodoListActivity.this, TodoItemActivity.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_TODO_ITEM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user created a todo_item.
                String title = data.getStringExtra(TodoItemActivity.EXTRA_TITLE);
                String desc = data.getStringExtra(TodoItemActivity.EXTRA_DESCRIPTION);
//                if(title != null) {
                if (desc == null) desc = "";
                TodoItem item = addTodoItem(title, desc);
                addTodoToDB(item);
//                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == MODIFY_TODO_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                // The user created a todo_item.
                int id = data.getIntExtra(EXTRA_ID, -1);
                String title = data.getStringExtra(TodoItemActivity.EXTRA_TITLE);
                String desc = data.getStringExtra(TodoItemActivity.EXTRA_DESCRIPTION);
//                if(title != null) {
                if (desc == null) desc = "";
                TodoItem item = addTodoItem(title, desc);
                addTodoToDB(item);
//                }
            }
        }
    }

    @Override
    protected void onStop() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TodoItemEntry.TABLE_NAME, null, null);

        for(TodoItem item : todolist) {
            System.out.println(item.title);
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TodoItemEntry.COLUMN_NAME_ITEM_ID, item.id);
            values.put(TodoItemEntry.COLUMN_NAME_TITLE, item.title);
            values.put(TodoItemEntry.COLUMN_NAME_DESCRIPTION, item.description);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.update(
                    TodoItemEntry.TABLE_NAME,
                    values,
                    null,
                    null);


        }

//        LinearLayout layout = (LinearLayout) findViewById(R.id.todolist);
//        layout.removeAllViewsInLayout();

        super.onStop();
    }

    @Override
    protected void onStart() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TodoItemEntry._ID,
                TodoItemEntry.COLUMN_NAME_TITLE,
                TodoItemEntry.COLUMN_NAME_DESCRIPTION,
//        ...
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                TodoItemEntry._ID + " ASC";

        Cursor c = db.query(
                TodoItemEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        todolist.clear();

        if(c.moveToFirst())
            do {
                int itemId = c.getInt(
                        c.getColumnIndexOrThrow(TodoItemEntry._ID)
                );
                String itemTitle = c.getString(
                        c.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_TITLE)
                );
                String itemDesc = c.getString(
                        c.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_DESCRIPTION)
                );

                System.out.println(itemTitle + " " + itemId);
                addTodoItem(itemId, itemTitle, itemDesc);
            } while(c.moveToNext());

        c.moveToFirst();
        String[] fromColumns = {TodoItemEntry.COLUMN_NAME_TITLE,
                TodoItemEntry.COLUMN_NAME_DESCRIPTION};
        int[] toViews = {R.id.todo_item1_title, R.id.todo_item1_description};

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_todo_item, c, fromColumns, toViews, 0);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        super.onStart();
    }

//    public void addTodo(View v) {
//        EditText todoMsg = (EditText) findViewById(R.id.add_todo_msg);
//
//        addTodoItem(todoMsg.getText().toString(), "");
//        todoMsg.setText("");
//    }

    public TodoItem addTodoItem(String title, String desc) {
        return addTodoItem(todolist.size() + 1, title, desc);
    }

    public TodoItem addTodoItem(int id, String title, String desc) {
//        todolist += title + " " + desc + "\n";
        TodoItem item = new TodoItem(id, title, desc);
        todolist.add(item);
        TextView itemView = new TextView(this);
        itemView.setText(title + "  " + desc);
//        LinearLayout layout = (LinearLayout) findViewById(R.id.todolist);
//        layout.addView(itemView);

        return item;
//        thingsTodo.setText(todolist);
    }

    void addTodoToDB(TodoItem item) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        System.out.println(item.title);
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TodoItemEntry.COLUMN_NAME_ITEM_ID, item.id);
        values.put(TodoItemEntry.COLUMN_NAME_TITLE, item.title);
        values.put(TodoItemEntry.COLUMN_NAME_DESCRIPTION, item.description);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TodoItemEntry.TABLE_NAME,
                null,
                values);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, null, PROJECTION, null, null, null) {
            @Override
            public Cursor loadInBackground()
            {
                // You better know how to get your database.
                SQLiteDatabase DB = mDbHelper.getReadableDatabase();
                // You can use any query that returns a cursor.
                return DB.query(TodoItemEntry.TABLE_NAME, getProjection(), getSelection(), getSelectionArgs(), null, null, getSortOrder(), null );
            }
        };
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    // When a todo_item is clicked
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(TodoListActivity.this, TodoItemActivity.class);
        TodoItem edit_item = todolist.get(position);
        System.out.println("Editing item # " + edit_item.id + "  " + edit_item.title);
        intent.putExtra(EXTRA_ID, edit_item.id);
        intent.putExtra(EXTRA_TITLE, edit_item.title);
        intent.putExtra(EXTRA_DESCRIPTION, edit_item.description);
        startActivityForResult(intent, MODIFY_TODO_ITEM_REQUEST);
    }
}
