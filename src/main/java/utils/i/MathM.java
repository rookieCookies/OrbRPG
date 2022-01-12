package utils.i;

import orbrpg.OrbRPG;

public class MathM {
    MathM() { throw new IllegalStateException("Utility class"); }
    public static boolean chanceOf(double chance) {
        float i = (OrbRPG.getInstance().getRand().nextFloat() * 100);
        System.out.println(i);
        return i <= chance;
    }
}
