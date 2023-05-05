package Models;

import android.content.Context;

public class common {
    public static int getPixelfromdp(Context context, float dp){
        float density = context.getResources().getDisplayMetrics().density;
        float px = dp * density;
        return  (int)px;
    }
}
