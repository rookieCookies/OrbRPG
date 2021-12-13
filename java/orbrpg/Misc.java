package orbrpg;

import org.bukkit.ChatColor;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.Level;

public class Misc {
    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();
    Misc() { throw new IllegalStateException("Utility class"); }
    static {
        ROMAN_NUMERALS.put(1000, "M");
        ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");
        ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");
        ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");
        ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");
        ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");
        ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");
        ROMAN_NUMERALS.put(0, "0");
    }
    public static String toRoman(int number) {
        int l = ROMAN_NUMERALS.floorKey(number);
        if (number == l) {
            return ROMAN_NUMERALS.get(number);
        }
        return ROMAN_NUMERALS.get(l) + toRoman(number - l);
    }
    public static String coloured(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    public static String formatNumber(float number) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        return nf.format(Math.round(number));
    }
    public static String getMessage(String path) {
        String message = OrbRPG.getInstance().getLanguageFile().getString(path);
        if (message == null) {
            OrbRPG.getInstance().getLogger().log(Level.WARNING,
                    "There is a message missing in the language file! Path: {0}",
                    path.replace(".", " > "));
            return "";
        }
        return Misc.coloured(message);
    }
}
