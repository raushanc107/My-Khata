package Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class mainModel {

    public HashMap<Integer,user> Users;
    public mainModel(){

        Users=new HashMap<Integer,user>();
    }

    public void addDatatoUser(int id, int userdataid,String username, double give, double got, Date date,String desc){

        if(!Users.containsKey(id)){
            user u=new user(username,id);
            Users.put(id,u);
        }
        if(userdataid!=0) {
            Users.get(id).AdduserData(userdataid, give, got, date,desc);
        }

    }

    public double getWillgive(){
        double willgive=0;
        for (Map.Entry<Integer,user> entry : Users.entrySet())
        {
            if(entry.getValue().balance<0){
                willgive+=entry.getValue().balance;
            }
        }

        return Math.abs(willgive);
    }

    public double getWillget(){
        double willgive=0;
        for (Map.Entry<Integer,user> entry : Users.entrySet())
        {
            if(entry.getValue().balance>0){
                willgive+=Math.abs(entry.getValue().balance);
            }
        }

        return Math.abs(willgive);
    }

    public double getBalance(){
        return getWillget()-getWillgive();
    }

}
