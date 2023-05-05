package Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykhata.R;
import com.example.mykhata.userdataActivity;

import org.intellij.lang.annotations.Language;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import DBHandler.DBHandler;
import Models.userData;

public class RecycleruserdataactivityAdapter extends RecyclerView.Adapter<RecycleruserdataactivityAdapter.ViewHolder> {
    Context context;
    ArrayList<userData> Data;
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
    DBHandler DB;
    public RecycleruserdataactivityAdapter(Context context, ArrayList<userData> data){
        this.context=context;
        this.Data=data;
        DB=new DBHandler(context);
        Collections.sort(Data, new Comparator<userData>() {
            @Override
            public int compare(userData o1, userData o2) {
                if(o2.date.compareTo(o1.date)==0){
                    return o2.id>o1.id?1:o2.id<o1.id?-1:0;
                }
                return o2.date.compareTo(o1.date);
            }
        });


    }
    @NonNull
    @Override
    public RecycleruserdataactivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.userdatarecyclerviewlayout,parent,false);
        RecycleruserdataactivityAdapter.ViewHolder viewHolder=new RecycleruserdataactivityAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleruserdataactivityAdapter.ViewHolder holder, int position) {

        holder.date.setText(new SimpleDateFormat("dd").format(Data.get(position).date));
        holder.month.setText(new SimpleDateFormat("MMM").format(Data.get(position).date));
        holder.year.setText(new SimpleDateFormat("yyyy").format(Data.get(position).date));

        holder.desc.setText(Data.get(position).desc);
        if(Data.get(position).got==0){
            holder.amount.setText("\u20B9 "+String.valueOf(Math.round(Data.get(position).give)));
            holder.amount.setTextColor(context.getResources().getColor(R.color.redtext));
            holder.amount.setBackgroundResource(R.color.redback);
        }
        if(Data.get(position).give==0){
            holder.amount.setText("\u20B9 "+String.valueOf(Math.round(Data.get(position).got)));
            holder.amount.setTextColor(context.getResources().getColor(R.color.greenText));
            holder.amount.setBackgroundResource(R.color.greenback);
        }
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView date,month,year,desc,amount;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.userdatalayoutdate);
            desc=itemView.findViewById(R.id.userdatalayoutdesc);
            amount=itemView.findViewById(R.id.userdatalayoutinout);
            month=itemView.findViewById(R.id.userdatalayoutdatemonth);
            year=itemView.findViewById(R.id.userdatalayoutdateyear);
            layout=itemView.findViewById(R.id.userdatalayout);
            layout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
            popupMenu.inflate(R.menu.popmenuupdatedelete);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
            return false;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){
                case R.id.editoption:{
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.giveandgotdialoglayout);
                    TextView t=dialog.findViewById(R.id.giveandgottext);
                    EditText amount=dialog.findViewById(R.id.giveorgotamount);
                    EditText desc=dialog.findViewById(R.id.giveorgotdesc);
                    Button d=dialog.findViewById(R.id.giveorgotdate);
                    Button btn=dialog.findViewById(R.id.giveorgotbtn);
                    ((userdataActivity)context).myday=Integer.parseInt(new SimpleDateFormat("dd").format(Data.get(getAdapterPosition()).date));
                    ((userdataActivity)context).mymonth=Integer.parseInt(new SimpleDateFormat("MM").format(Data.get(getAdapterPosition()).date));
                    ((userdataActivity)context).myyear=Integer.parseInt(new SimpleDateFormat("yyyy").format(Data.get(getAdapterPosition()).date));
                    d.setText(((userdataActivity)context).myday+"-"+((userdataActivity)context).mymonth+"-"+((userdataActivity)context).myyear);
                    btn.setText("Update");
                    desc.setText(Data.get(getAdapterPosition()).desc);
                    if(Data.get(getAdapterPosition()).give==0){
                        t.setText("YOU GOT");
                        amount.setText(String.valueOf(Math.round(Data.get(getAdapterPosition()).got)));
                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        ((userdataActivity)context).myday=dayOfMonth;
                                        ((userdataActivity)context).mymonth=month+1;
                                        ((userdataActivity)context).myyear=year;
                                        d.setText(((userdataActivity)context).myday+"-"+(((userdataActivity)context).mymonth)+"-"+((userdataActivity)context).myyear);
                                    }
                                },((userdataActivity)context).myyear,(((userdataActivity)context).mymonth-1),((userdataActivity)context).myday);

                                datePickerDialog.show();

                            }
                        });
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                                    DB.UpdateUserData(Data.get(getAdapterPosition()).id,((userdataActivity)context).User.id,amount.getText().toString(),"",((userdataActivity)context).myday+"-"+((userdataActivity)context).mymonth+"-"+((userdataActivity)context).myyear,desc.getText().toString());
                                    ((userdataActivity)context).load();
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.show();
                    }
                    if(Data.get(getAdapterPosition()).got==0){
                        t.setText("YOU GAVE");
                        amount.setText(String.valueOf(Math.round(Data.get(getAdapterPosition()).give)));
                        d.setText(((userdataActivity)context).myday+"-"+((userdataActivity)context).mymonth+"-"+((userdataActivity)context).myyear);
                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        ((userdataActivity)context).myday=dayOfMonth;
                                        ((userdataActivity)context).mymonth=month+1;
                                        ((userdataActivity)context).myyear=year;
                                        d.setText(((userdataActivity)context).myday+"-"+(((userdataActivity)context).mymonth)+"-"+((userdataActivity)context).myyear);
                                    }
                                },((userdataActivity)context).myyear,(((userdataActivity)context).mymonth-1),((userdataActivity)context).myday);

                                datePickerDialog.show();

                            }
                        });
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!amount.getText().toString().equals("")&&!desc.getText().toString().equals("")) {
                                    DB.UpdateUserData(Data.get(getAdapterPosition()).id,((userdataActivity)context).User.id,"",amount.getText().toString(),((userdataActivity)context).myday+"-"+((userdataActivity)context).mymonth+"-"+((userdataActivity)context).myyear,desc.getText().toString());
                                    ((userdataActivity)context).load();
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.show();
                    }

                    return true;
                }
                case R.id.deleteoption:{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Are you sure you want to Delete This Entry?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            DB.DeleteUserData(Data.get(getAdapterPosition()).id);
                            ((userdataActivity)context).load();
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
                    return true;
                }
                default:{
                    return false;
                }
            }
        }
    }
}
