package utils.i;

import java.text.NumberFormat;
import java.util.Locale;

public class StringM {
    StringM() { throw new IllegalStateException("Utility class"); }
    public static String getFormattedNumber(float number) {
        var nf = NumberFormat.getInstance(new Locale("en", "US"));
        return nf.format(NumberM.fixDouble(number));
    }
}
