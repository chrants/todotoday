package com.christiantschoepe.todotoday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodoItemActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "todolist.ITEM_ID";
    public final static String EXTRA_TITLE = "todolist.ITEM_TITLE";
    public final static String EXTRA_DESCRIPTION = "todolist.ITEM_DESCRIPTION";

    EditText itemTitle;
    EditText itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemTitle = (EditText) findViewById(R.id.todo_item_title);
        itemDescription = (EditText) findViewById(R.id.todo_item_description);

        Intent intent = getIntent();
        final int id = intent.getIntExtra(TodoItemActivity.EXTRA_ID, -1);
        String title = intent.getStringExtra(TodoItemActivity.EXTRA_TITLE);
        String desc = intent.getStringExtra(TodoItemActivity.EXTRA_DESCRIPTION);
        boolean isNewItem = title == null;
        if(title != null) {
            itemTitle.setText(title);
            itemDescription.setText(desc);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoItemActivity.this, TodoListActivity.class);
                String title = itemTitle.getText().toString();
                String desc = itemDescription.getText().toString();

                if(title.trim().equals("")) {
                    Snackbar.make(view, "Please put a valid title", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    intent.putExtra(EXTRA_ID, id);
                    intent.putExtra(EXTRA_TITLE, title);
                    intent.putExtra(EXTRA_DESCRIPTION, desc);
//                    startActivity(intent);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TextView title = new TextView(this);
//        title.setText("My item title");
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.todo_content);
//        layout.addView(title);
    }

}
