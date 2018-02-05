package com.example.atharv.atharv_assignment1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.provider.Telephony.Mms.Part.FILENAME;

/** Main activity that handles the subscription list and activity intent sending **/

public class MainActivity extends AppCompatActivity {

    // Creating ListView, arraylist and adapter

    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList <Subscription> subList = new ArrayList<Subscription>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Started with basic activity layout from android default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting adapter and listView to register in layout, onCreate

        listView = (ListView) findViewById(R.id.Lview);
        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, subList);

        listView.setAdapter(listViewAdapter);

        // Listener for click (for editing)

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editIntent = new Intent(MainActivity.this, EditClass.class);
                Subscription editsub = subList.get(i);
                editIntent.putExtra("Name", editsub.getName());
                editIntent.putExtra("Comment", editsub.getComment());
                editIntent.putExtra("Charge", editsub.getCharge());
                editIntent.putExtra("Date", editsub.getStartDate());
                editIntent.putExtra("Index", i);

                startActivityForResult(editIntent, 666);
            }
        });

        // Listener for long click (delete)

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                subList.remove(i);
                listViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // Floating action button stuff

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddSubActivity(view);
            }
        });

        // Basic setting on app open/activity create
        loadFromFile();
        getTotal();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        // Function to get result after sending intent, used to parse data from one activity to another

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 42){
            // Result for adding sub
            if(resultCode == RESULT_OK){
                // Retrieving new data
                String name = data.getExtras().getString("NAME");
                String charge = data.getExtras().getString("CHARGE");
                String comment = data.getExtras().getString("COMMENT");
                String date = data.getExtras().getString("DATE");
                Subscription sub = new Subscription(name, charge, date, comment);

                subList.add(sub);
                listViewAdapter.notifyDataSetChanged();
            }
        }
        if(requestCode == 666){
            // Result for editing sub
            if(resultCode == RESULT_OK){
                // Retrieving edited data
                String name = data.getExtras().getString("NAME");
                String charge = data.getExtras().getString("CHARGE");
                String comment = data.getExtras().getString("COMMENT");
                String date = data.getExtras().getString("DATE");
                int index = data.getExtras().getInt("INDEX");
                Subscription sub = new Subscription(name, charge, date, comment);
                subList.set(index, sub);
                listViewAdapter.notifyDataSetChanged();
            }
        }
        getTotal(); // Fucntion call to update actionBar

    }

    @Override
    protected void onDestroy(){
        // On destroy, dave data
        super.onDestroy();
        saveToFile();
    }

    public void saveToFile(){

        // Function to save data to file
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson(); //Gson object for parsing

            gson.toJson(subList, out); //Writing

            out.flush();

            Log.e("SAVE IN FILE", "kill me");

            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadFromFile(){
        // Function to load data from file onCreate
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson(); //Gson object fr parsing

            //Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-24

            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType(); //Getting

            ArrayList<Subscription> newsList = gson.fromJson(in, listType);
            subList.clear();
            subList.addAll(newsList);
            listViewAdapter.notifyDataSetChanged();
            Log.e("LOAD FROM FILE", "haha");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            subList = new ArrayList<Subscription>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    // Function to send to the second activity
    public void gotoAddSubActivity(View view) {
        Intent intent = new Intent(this, AddSubActivity.class);
        startActivityForResult(intent, 42);
    }

    // Function to get total and update action bar
    public void getTotal(){
        double total = 0;
        for(Subscription sub: subList){
            total += Double.valueOf(sub.getCharge());
        }
        getSupportActionBar().setTitle("Total: " + total);
    }
}
