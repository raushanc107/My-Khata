package com.example.mykhata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import Adapters.DailyexpenseAdapter;
import Adapters.RecycleruserdataactivityAdapter;
import DBHandler.DBHandler;
import Models.PersonalExpenceData;
import Models.PersonalExpenseModel;

public class PersonalExpense extends AppCompatActivity {
    public int myyear,mymonth,myday;
    DBHandler DB;
    final Calendar calendar=Calendar.getInstance();
    RecyclerView PersonalExpenseRV;
    TextView dailyexpancebalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_expense);
        setTitle("Daily Expense");
        getSupportActionBar().setElevation(0);
        DB=new DBHandler(this);
        PersonalExpenseRV=findViewById(R.id.dailyexpenseBalance);
        PersonalExpenseRV.setLayoutManager(new LinearLayoutManager(this));
        dailyexpancebalance=findViewById(R.id.dailyexpancebalance);
        Load();
    }


    public void Load(){
        PersonalExpenseModel data=DB.getPersonalExpenseData();
        dailyexpancebalance.setText("\u20B9 "+Integer.toString(Math.round((float)data.Balance)));
        if(data.Balance>0){
            dailyexpancebalance.setTextColor(getResources().getColor(R.color.greenText));
        } if(data.Balance<0){
            dailyexpancebalance.setTextColor(getResources().getColor(R.color.redtext));
        }
        DailyexpenseAdapter adapter=new DailyexpenseAdapter(this,data.data);
        PersonalExpenseRV.setAdapter(adapter);
    }



    public void dailyINClicked(View v){

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.giveandgotdialoglayout);
        TextView t=dialog.findViewById(R.id.giveandgottext);
        t.setText("Money In");
        EditText amount=dialog.findViewById(R.id.giveorgotamount);
        EditText desc=dialog.findViewById(R.id.giveorgotdesc);
        Button d=dialog.findViewById(R.id.giveorgotdate);
        myyear = calendar.get(Calendar.YEAR);
        mymonth=calendar.get(Calendar.MONTH);
        myday=calendar.get(Calendar.DAY_OF_MONTH);
        d.setText(myday+"-"+(mymonth+1)+"-"+myyear);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(PersonalExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myday=dayOfMonth;
                        mymonth=month;
                        myyear=year;
                        d.setText(myday+"-"+(mymonth+1)+"-"+myyear);
                    }
                },myyear,mymonth,myday);

                datePickerDialog.show();

            }
        });
        Button btn=dialog.findViewById(R.id.giveorgotbtn);
        btn.setText("ADD");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                    DB.InsertPersonalDataExpense(amount.getText().toString(),desc.getText().toString(),"IN",myday+"-"+(mymonth+1)+"-"+myyear);
                    Load();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void dailyOutClicked(View v){

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.giveandgotdialoglayout);
        TextView t=dialog.findViewById(R.id.giveandgottext);
        t.setText("Money Out");
        EditText amount=dialog.findViewById(R.id.giveorgotamount);
        EditText desc=dialog.findViewById(R.id.giveorgotdesc);
        Button d=dialog.findViewById(R.id.giveorgotdate);
        myyear = calendar.get(Calendar.YEAR);
        mymonth=calendar.get(Calendar.MONTH);
        myday=calendar.get(Calendar.DAY_OF_MONTH);
        d.setText(myday+"-"+(mymonth+1)+"-"+myyear);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(PersonalExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myday=dayOfMonth;
                        mymonth=month;
                        myyear=year;
                        d.setText(myday+"-"+(mymonth+1)+"-"+myyear);
                    }
                },myyear,mymonth,myday);

                datePickerDialog.show();

            }
        });
        Button btn=dialog.findViewById(R.id.giveorgotbtn);
        btn.setText("ADD");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                    DB.InsertPersonalDataExpense(amount.getText().toString(),desc.getText().toString(),"OUT",myday+"-"+(mymonth+1)+"-"+myyear);
                    Load();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}