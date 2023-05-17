package com.example.mykhata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import Adapters.RecyclerViewMainActivityAdapter;
import DBHandler.DBHandler;
import Models.mainModel;
import Models.user;
import swipehandler.SwipeController;
import swipehandler.SwipeControllerActions;

public class MainActivity extends AppCompatActivity {
    DBHandler DB;
    TextView willgive,willget,balance;
    RecyclerView recyclerView;
    SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        DB=new DBHandler(this);
        willget=findViewById(R.id.willget);
        willgive=findViewById(R.id.willgive);
        recyclerView=findViewById(R.id.contactlist);
        balance=findViewById(R.id.balanceamount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dailyexpense:
                Intent i = new Intent(this, PersonalExpense.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    public void loadPage(){
        mainModel model=DB.getDataFromDB();
        double Balance=model.getBalance();
        double Willgive=model.getWillgive();
        double Willget=model.getWillget();
        willgive.setText("\u20B9 "+Integer.toString(Math.round((float)Willgive)));
        willget.setText("\u20B9 "+Integer.toString(Math.round((float)Willget)));
        balance.setText("\u20B9 "+Integer.toString(Math.round((float)Balance)));

        if(Balance>0){
          balance.setTextColor(getResources().getColor(R.color.greenText));
        }
        if(Balance<0){
            balance.setTextColor(getResources().getColor(R.color.redtext));
        }
        ArrayList<user> users=new ArrayList<user>();
        for (Map.Entry<Integer,user> entry : model.Users.entrySet())
        {
            users.add(entry.getValue());
        }
        RecyclerViewMainActivityAdapter adapter=new RecyclerViewMainActivityAdapter(this,users);
        recyclerView.setAdapter(adapter);
       // setupRecyclerView(adapter,users);


    }

    public void AddnewUser(View v){
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.addnewuserdialog);
        EditText editText=dialog.findViewById(R.id.textboxforaddnewuser);
        Button btnadd=dialog.findViewById(R.id.addnewuserbtn);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")&&editText.getText()!=null){
                    DB.InsertNewUser(editText.getText().toString());
                    loadPage();
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this,"Name can not be blank",Toast.LENGTH_SHORT);
                }

            }
        });
        dialog.show();
    }

    private void setupRecyclerView(RecyclerViewMainActivityAdapter mAdapter, ArrayList<user> users) {

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
//                mAdapter.users.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to Delete This Page? - "+users.get(position).name);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        DB.DeleteUsers(users.get(position).id);
                        loadPage();
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
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.addnewuserdialog);
                TextView tvlbl=dialog.findViewById(R.id.addnewlbl);
                EditText editText=dialog.findViewById(R.id.textboxforaddnewuser);
                tvlbl.setText("Edit Page Name");
                editText.setText(users.get(position).name);
                Button btnadd=dialog.findViewById(R.id.addnewuserbtn);
                btnadd.setText("Save");
                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editText.getText().toString().equals("")&&editText.getText()!=null){
                            DB.updateuserTable(users.get(position).id,editText.getText().toString());
                            loadPage();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this,"Name can not be blank",Toast.LENGTH_SHORT);
                        }

                    }
                });
                dialog.show();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}