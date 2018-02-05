package com.example.atharv.atharv_assignment1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddSubActivity extends AppCompatActivity {

    public String name;
    public String date;
    public String comment;
    public String charge;
    Button datepickBtn;
    Calendar c;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        datepickBtn = (Button) findViewById(R.id.pickDate);

        datepickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);

                dpd = new DatePickerDialog(AddSubActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date = year+"-"+(month+1)+"-"+day;
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }

    public void parseToMainActivity(View view){
        Intent changeData = new Intent(this, MainActivity.class);
        EditText inputName = (EditText) findViewById(R.id.editName);
        name = inputName.getText().toString();
        if(name == null || name.isEmpty()){
            Toast.makeText(AddSubActivity.this, "Did you really sign up for no subscription?", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText inputComment = (EditText) findViewById(R.id.editComments);
        comment = inputComment.getText().toString();
        EditText inputCharge = (EditText) findViewById(R.id.editText3);
        //inputCharge.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        charge = inputCharge.getText().toString();
        if(charge == null || charge.isEmpty()){
            Toast.makeText(AddSubActivity.this, "If it's empty, enter 0", Toast.LENGTH_SHORT).show();
            return;
        }
        if(date == null || date.isEmpty()){
            Toast.makeText(AddSubActivity.this, "I mean, time travel is impossible", Toast.LENGTH_SHORT).show();
            return;
        }

        changeData.putExtra("NAME", name);
        changeData.putExtra("CHARGE", charge);
        changeData.putExtra("DATE", date);
        changeData.putExtra("COMMENT", comment);

        setResult(RESULT_OK, changeData);
        finish();
    }

}
