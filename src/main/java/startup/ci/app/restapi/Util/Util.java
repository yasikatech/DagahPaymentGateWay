package startup.ci.app.restapi.Util;

import java.util.Date;

public class Util {


    private static final double currencyChangeRatio = 0.812D;


    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * currencyChangeRatio);
    }

    public static int convertEuroToDollar(int dollars) {
        return (int) Math.round(dollars / currencyChangeRatio);
    }


    public static Date getTodayDate() {
        Date today = new Date();
        //  return formatDate(calendar);
        return today;
    }

}
