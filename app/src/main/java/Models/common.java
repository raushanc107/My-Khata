package Models;

import android.content.Context;
import java.text.DecimalFormat;

public class common {
    public static int getPixelfromdp(Context context, float dp){
        float density = context.getResources().getDisplayMetrics().density;
        float px = dp * density;
        return  (int)px;
    }

    public static String formatCurrency(double amount) {
        long roundedAmount = Math.round(amount);
        DecimalFormat decimalFormat = new DecimalFormat("#,##,##,##0");
        return "\u20B9 " + decimalFormat.format(Math.abs(roundedAmount));
    }
}
