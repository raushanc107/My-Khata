package com.example.mykhata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import Adapters.RecyclerViewMainActivityAdapter;
import DBHandler.DBHandler;
import Models.common;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.action_bar_background)));
        DB=new DBHandler(this);
        willget=findViewById(R.id.willget);
        willgive=findViewById(R.id.willgive);
        recyclerView=findViewById(R.id.contactlist);
        balance=findViewById(R.id.balanceamount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export_db) {
            exportDb();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportDb() {
        try {
            File dbFile = getDatabasePath(DB.getDatabaseName());
            String fileName = "mykhatabookdb.db";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/x-sqlite3");

            Uri externalUri = MediaStore.Files.getContentUri("external");
            Uri uri = getContentResolver().insert(externalUri, values);

            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri);
                     FileInputStream inputStream = new FileInputStream(dbFile)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    Toast.makeText(this, "Database exported to Downloads folder", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to export database", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to create file in Downloads folder", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        willgive.setText(common.formatCurrency(Willgive));
        willget.setText(common.formatCurrency(Willget));
        balance.setText(common.formatCurrency(Balance));

        if(Balance>0){
          balance.setTextColor(ContextCompat.getColor(this, R.color.greenText));
        }
        if(Balance<0){
            balance.setTextColor(ContextCompat.getColor(this, R.color.redtext));
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
