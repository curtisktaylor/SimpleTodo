package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //when user is done editing
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent with results
                Intent intent = new Intent();
                //pass results back to main
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //set result
                setResult(RESULT_OK, intent);
                //finish activity
                finish();
            }
        });
    }
}