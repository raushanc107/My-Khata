package DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Models.mainModel;
import Models.user;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="mykhatabookdb";
    private static final int DATAVASE_VERSION=2;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATAVASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE users(id INTEGER,name TEXT,PRIMARY KEY(id AUTOINCREMENT))";
        String query2="CREATE TABLE userdata(id INTEGER,pk_id INTEGER,got TEXT,give TEXT,date TEXT,descr TEXT,PRIMARY KEY(id AUTOINCREMENT))";

        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS userdata");
        onCreate(db);

    }

    public mainModel getDataFromDB(){
        SQLiteDatabase db=this.getReadableDatabase();
        mainModel model=new mainModel();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try{
            String query="select u.id as userid,u.name as username,ud.id as dataid,ud.give,ud.got,ud.date,ud.descr from users  u left join userdata ud on u.id=ud.pk_id";
            Cursor cursor=db.rawQuery(query,null);
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                int dataid=cursor.getInt(2);
                double give=cursor.getString(3)==null?0:cursor.getString(3).equals("")?0:Double.parseDouble(cursor.getString(3));
                double got=cursor.getString(4)==null?0:cursor.getString(4).equals("")?0:Double.parseDouble(cursor.getString(4));
                Date date=cursor.getString(5)==null?new Date():formatter.parse(cursor.getString(5));
                String desc=cursor.getString(6)==null?"":cursor.getString(6);
                model.addDatatoUser(id,dataid,name,give,got,date,desc);

            }

        }
        catch (Exception e)
        {
            Log.e("exec",e.getMessage());
        }finally {
            db.close();
        }
        return model;
    }

    public user getUserData(int userid){
        mainModel model=getDataFromDB();
        return model.Users.get(userid);
    }

    public void InsertNewUser(String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            ContentValues values=new ContentValues();
            values.put("name",Username);
            db.insert("users",null,values);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }

    public void InsertUserData(int uid,String got,String give,String date,String descr){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            ContentValues values=new ContentValues();
            values.put("pk_id",uid);
            values.put("got",got);
            values.put("give",give);
            values.put("date",date);
            values.put("descr",descr);
            db.insert("userdata",null,values);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }

    public void UpdateUserData(int id,int uid,String got,String give,String date,String descr){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            ContentValues values=new ContentValues();
            values.put("pk_id",uid);
            values.put("got",got);
            values.put("give",give);
            values.put("date",date);
            values.put("descr",descr);
            db.update("userdata",values,"id="+id,null);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }
    public void updateuserTable(int id,String newName){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            ContentValues values=new ContentValues();
            values.put("name",newName);
            db.update("users",values,"id="+id,null);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }

    public void DeleteUsers(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            db.delete("userdata","pk_id="+id,null);
            db.delete("users","id="+id,null);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }

    public void DeleteUserData(int id) {
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            db.delete("userdata","id="+id,null);

        }catch (Exception e){

        }
        finally {
            db.close();
        }
    }
}
