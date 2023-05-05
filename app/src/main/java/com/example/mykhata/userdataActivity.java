package com.example.mykhata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Adapters.RecyclerViewMainActivityAdapter;
import Adapters.RecycleruserdataactivityAdapter;
import DBHandler.DBHandler;
import Models.user;
import Models.userData;
import swipehandler.SwipeController;
import swipehandler.SwipeControllerActions;

public class userdataActivity extends AppCompatActivity {
    public user User;
    public int Userid;
    DBHandler DB;
    public int myyear,mymonth,myday;
    SwipeController swipeController = null;
    TextView finaltext,finalvalue,title,titleicon;
    RecyclerView userdatarecyclerView;
    final Calendar calendar=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        getSupportActionBar().setElevation(0);
        Userid=getIntent().getIntExtra("userid",0);
        DB=new DBHandler(this);
        User=DB.getUserData(Userid);
        setTitle(User.name);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        finaltext=findViewById(R.id.peruserfinaltext);
        finalvalue=findViewById(R.id.peruserfinalvalue);
        userdatarecyclerView=findViewById(R.id.userdatarecyclerView);
        userdatarecyclerView.setLayoutManager(new LinearLayoutManager(this));
        title=findViewById(R.id.activityTitle);
        titleicon=findViewById(R.id.activitytitleicon);
        title.setText(User.name);
        String[] names=User.name.toUpperCase().split(" ");
        titleicon.setText(names[0].charAt(0)+""+names[names.length-1].charAt(0));
        load();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void load(){
        User=DB.getUserData(Userid);
        double totalvalue=User.balance;
        finalvalue.setText("\u20B9 "+Integer.toString(Math.round((float)Math.abs(totalvalue))));
        if(totalvalue>0){
            finaltext.setText("You will get");
            finalvalue.setTextColor(getResources().getColor(R.color.greenText));
        }
        if(totalvalue<0){
            finaltext.setText("You will give");
            finalvalue.setTextColor(getResources().getColor(R.color.redtext));
        }
        if(totalvalue==0){
            finaltext.setText("No Dues");
        }

        RecycleruserdataactivityAdapter adapter=new RecycleruserdataactivityAdapter(this,User.userdata);
        userdatarecyclerView.setAdapter(adapter);
        //setupRecyclerView(adapter,User.userdata);

    }

    public void yougotclicked(View v){

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.giveandgotdialoglayout);
        TextView t=dialog.findViewById(R.id.giveandgottext);
        t.setText("YOU GOT");
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

                DatePickerDialog datePickerDialog=new DatePickerDialog(userdataActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    DB.InsertUserData(User.id,amount.getText().toString(),"",myday+"-"+(mymonth+1)+"-"+myyear,desc.getText().toString());
                    load();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void yougiveclicked(View v){

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.giveandgotdialoglayout);
        TextView t=dialog.findViewById(R.id.giveandgottext);
        t.setText("YOU GAVE");
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

                DatePickerDialog datePickerDialog=new DatePickerDialog(userdataActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    DB.InsertUserData(User.id,"",amount.getText().toString(),myday+"-"+(mymonth+1)+"-"+myyear,desc.getText().toString());
                    load();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    private void setupRecyclerView(RecycleruserdataactivityAdapter mAdapter, ArrayList<userData> users) {

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
//                mAdapter.users.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                AlertDialog.Builder builder = new AlertDialog.Builder(userdataActivity.this);

                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to Delete This Entry?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        DB.DeleteUserData(users.get(position).id);
                        load();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }

            @Override
            public void onLeftClicked(int position) {
                Dialog dialog=new Dialog(userdataActivity.this);
                dialog.setContentView(R.layout.giveandgotdialoglayout);
                TextView t=dialog.findViewById(R.id.giveandgottext);
                EditText amount=dialog.findViewById(R.id.giveorgotamount);
                EditText desc=dialog.findViewById(R.id.giveorgotdesc);
                Button d=dialog.findViewById(R.id.giveorgotdate);
                Button btn=dialog.findViewById(R.id.giveorgotbtn);
                myday=Integer.parseInt(new SimpleDateFormat("dd").format(users.get(position).date));
                mymonth=Integer.parseInt(new SimpleDateFormat("MM").format(users.get(position).date));
                myyear=Integer.parseInt(new SimpleDateFormat("yyyy").format(users.get(position).date));
                d.setText(myday+"-"+mymonth+"-"+myyear);
                btn.setText("Update");
                desc.setText(users.get(position).desc);
                if(users.get(position).give==0){
                    t.setText("YOU GOT");
                    amount.setText(String.valueOf(Math.round(users.get(position).got)));
                    d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatePickerDialog datePickerDialog=new DatePickerDialog(userdataActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    myday=dayOfMonth;
                                    mymonth=month;
                                    myyear=year;
                                    d.setText(myday+"-"+mymonth+"-"+myyear);
                                }
                            },myyear,mymonth,myday);

                            datePickerDialog.show();

                        }
                    });
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                                DB.UpdateUserData(users.get(position).id,User.id,amount.getText().toString(),"",myday+"-"+mymonth+"-"+myyear,desc.getText().toString());
                                load();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
                if(users.get(position).got==0){
                    t.setText("YOU GAVE");
                    amount.setText(String.valueOf(Math.round(users.get(position).give)));
                    d.setText(myday+"-"+mymonth+"-"+myyear);
                    d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatePickerDialog datePickerDialog=new DatePickerDialog(userdataActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    myday=dayOfMonth;
                                    mymonth=month;
                                    myyear=year;
                                    d.setText(myday+"-"+mymonth+"-"+myyear);
                                }
                            },myyear,mymonth,myday);

                            datePickerDialog.show();

                        }
                    });
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                                DB.UpdateUserData(users.get(position).id,User.id,"",amount.getText().toString(),myday+"-"+mymonth+"-"+myyear,desc.getText().toString());
                                load();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }

            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(userdatarecyclerView);

        userdatarecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}