package Models;

import java.util.Date;

public class userData {

    public int id;
    public double give;
    public double got;

    public String desc;
    public Date date;

    public userData(int id,double give,double got,Date date,String desc){
        this.id=id;
        this.give=give;
        this.got=got;
        this.date=date;
        this.desc=desc;
    }

}
