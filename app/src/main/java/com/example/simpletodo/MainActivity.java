package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter ItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.buttonAdd);
        etItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.itemList);

        loadItems();


        ItemsAdapter.OnLongClickListener OnLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                String deletedItem = items.get(position);
                //delete item from list
                items.remove(position);
                //notify adapter of deleted item
                ItemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), deletedItem + " was removed.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener OnClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "" + position);
                //create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass info being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display new activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        ItemsAdapter = new ItemsAdapter(items, OnLongClickListener, OnClickListener);
        rvItems.setAdapter(ItemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //Add item to model
                items.add(todoItem);
                //Notify adapter that item was insterted
                ItemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), todoItem + " was added.", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


    }

    //handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //get updated text
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //get original position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //update model with new text
            items.set(position, itemText);
            //notify adapter with change
            ItemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item changed to " + itemText, Toast.LENGTH_SHORT).show();
        } else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //loads items by reading data.txt
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //writes list data back to data.txt
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch(IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}