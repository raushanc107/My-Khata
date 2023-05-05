package Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykhata.MainActivity;
import com.example.mykhata.R;
import com.example.mykhata.userdataActivity;
import com.google.android.material.appbar.AppBarLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import DBHandler.DBHandler;
import Models.user;

public class RecyclerViewMainActivityAdapter extends RecyclerView.Adapter<RecyclerViewMainActivityAdapter.ViewHolder> {
    Context context;
    public ArrayList<user> users;
    DBHandler DB;
    public RecyclerViewMainActivityAdapter(Context context,ArrayList<user> Users){
        this.context=context;
        this.users=Users;
        DB=new DBHandler(context);
    }

    @NonNull
    @Override
    public RecyclerViewMainActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.mainuserlist,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMainActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(users.get(position).name);
            String[] names=users.get(position).name.toUpperCase().split(" ");
            holder.icon.setText(names[0].charAt(0)+""+names[names.length-1].charAt(0));
            double totalvalue=users.get(position).balance;
        holder.amount.setText("\u20B9 "+Integer.toString(Math.round((float)Math.abs(totalvalue))));
            if(totalvalue>0){

                holder.amount.setTextColor(context.getResources().getColor(R.color.greenText));
                holder.amount.setBackgroundResource(R.color.greenback);
            }
            if(totalvalue<0){

                holder.amount.setTextColor(context.getResources().getColor(R.color.redtext));
                holder.amount.setBackgroundResource(R.color.redback);
            }
            if(totalvalue==0){

                holder.amount.setBackgroundResource(R.color.lightoutline);

            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(context,userdataActivity.class);
                    i.putExtra("userid",users.get(position).id);
                    context.startActivity(i);
                }
            });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        TextView name,icon,amount;
        LinearLayout layout,editanddeletelayout,editlayout,deletelayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.contactname);
            icon=itemView.findViewById(R.id.usernameicon);
            amount=itemView.findViewById(R.id.contacttotalamount);
            layout=itemView.findViewById(R.id.usermainlayoutperuser);
            editanddeletelayout=itemView.findViewById(R.id.frameeditanddelete);
            editlayout=itemView.findViewById(R.id.frameedit);
            deletelayout=itemView.findViewById(R.id.framedelete);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                                PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
            popupMenu.inflate(R.menu.popmenuupdatedelete);
            popupMenu.setOnMenuItemClickListener(ViewHolder.this);
            popupMenu.show();
                    //editanddeletelayout.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            editlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditItem();
                }
            });

            deletelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteItem();
                }
            });
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.editoption: {
                    EditItem();
                    return true;

                }
                case R.id.deleteoption:{
                    DeleteItem();
                    return true;
                }
                default:{
                    return false;
                }

            }
        }




        private void EditItem(){

            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.addnewuserdialog);
            TextView tvlbl=dialog.findViewById(R.id.addnewlbl);
            EditText editText=dialog.findViewById(R.id.textboxforaddnewuser);
            tvlbl.setText("Edit Page Name");
            editText.setText(users.get(getAdapterPosition()).name);
            Button btnadd=dialog.findViewById(R.id.addnewuserbtn);
            btnadd.setText("Save");
            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!editText.getText().toString().equals("")&&editText.getText()!=null){
                        DB.updateuserTable(users.get(getAdapterPosition()).id,editText.getText().toString());
                        ((MainActivity)context).loadPage();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(context,"Name can not be blank",Toast.LENGTH_SHORT);
                    }

                }
            });
            dialog.show();
        }
        private void DeleteItem(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to Delete This Page? - "+users.get(getAdapterPosition()).name);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    DB.DeleteUsers(users.get(getAdapterPosition()).id);
                    ((MainActivity)context).loadPage();
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
    }
}
