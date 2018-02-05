package com.example.atharv.atharv_assignment1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/** Class activity for editing subscriptions **/

public class EditClass extends AddSubActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Getting sub data fromm main activity

        String name = getIntent().getStringExtra("Name");
        String comment = getIntent().getStringExtra("Comment");
        String charge = getIntent().getStringExtra("Charge");
        date = getIntent().getStringExtra("Date");

        EditText inputName = (EditText) findViewById(R.id.editName);
        EditText inputComment = (EditText) findViewById(R.id.editComments);
        EditText inputCharge = (EditText) findViewById(R.id.editText3);

        // Setting data to editTexts

        inputCharge.setText(charge);
        inputComment.setText(comment);
        inputName.setText(name);

        // Listener for calendar
        datepickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Converting parsed date to default the calendar

                String[] splitString = date.split("-");
                int year = Integer.valueOf(splitString[0]);
                int month= Integer.valueOf(splitString[1])-1;
                int day = Integer.valueOf(splitString[2]);

                dpd = new DatePickerDialog(EditClass.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        EditClass.this.date = year+"-"+(month+1)+"-"+day;
                    }
                }, year, month, day);
                dpd.show();
            }
        });

    }
    @Override
    public void parseToMainActivity(View view) {
        // Similar function to Add Sub Activity to add data to forResult intent

        Intent changeData = new Intent(this, MainActivity.class);
        EditText inputName = (EditText) findViewById(R.id.editName);
        name = inputName.getText().toString();
        EditText inputComment = (EditText) findViewById(R.id.editComments);
        comment = inputComment.getText().toString();
        EditText inputCharge = (EditText) findViewById(R.id.editText3);
        charge = inputCharge.getText().toString();

        // Index send for the edited subList object
        int index = getIntent().getIntExtra("Index", -1);

        changeData.putExtra("NAME", name);
        changeData.putExtra("CHARGE", charge);
        changeData.putExtra("DATE", date);
        changeData.putExtra("COMMENT", comment);
        changeData.putExtra("INDEX", index);

        setResult(RESULT_OK, changeData);
        finish();
    }
}


