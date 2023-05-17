package Models;

import java.util.ArrayList;

public class PersonalExpenseModel {
    public double Balance=0;
    public ArrayList<PersonalExpenceData> data;
    public PersonalExpenseModel(){
        Balance=0;
        data=new ArrayList<>();
    }
    public void AddData(PersonalExpenceData d){
        if(d.inout.equals("IN")){
            Balance+=d.amount;
        }
        if(d.inout.equals("OUT")){
            Balance-=d.amount;
        }
        data.add(d);

    }

}
