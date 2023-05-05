package Models;

import java.util.ArrayList;
import java.util.Date;

public class user {
    public int id;
    public String name;
    public double totalin=0;
    public double totalout=0;
    public double balance=0;
    public ArrayList<userData> userdata;
    public user(String name,int id){
        totalin=0;
        totalout=0;
        this.name=name;
        this.id=id;
        userdata=new ArrayList<userData>();
    }

    public void AdduserData(int id, double give, double got, Date date,String desc){
        userData t=new userData(id,give,got,date,desc);
        userdata.add(t);
        this.totalout+=give;
        this.totalin+=got;
        balance=totalout-totalin;
    }
}
