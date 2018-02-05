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

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList <Subscription> subList = new ArrayList<Subscription>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.Lview);
        listViewAdapter = new ListViewAdapter(this, R.layout.listviewitem, subList);

        listView.setAdapter(listViewAdapter);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                subList.remove(i);
                listViewAdapter.notifyDataSetChanged();
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddSubActivity(view);
            }
        });
        //subList.add(new Subscription());
        //listViewAdapter.notifyDataSetChanged();

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

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 42){
            if(resultCode == RESULT_OK){

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
            if(resultCode == RESULT_OK){

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
        getTotal();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveToFile();
    }

    public void saveToFile(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();

            gson.toJson(subList, out);

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
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-24

            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();

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

    public void gotoAddSubActivity(View view) {
        Intent intent = new Intent(this, AddSubActivity.class);
        startActivityForResult(intent, 42);
    }

    public void getTotal(){
        double total = 0;
        for(Subscription sub: subList){
            total += Double.valueOf(sub.getCharge());
        }
        getSupportActionBar().setTitle("Total: " + total);
    }
}
