package utils;

import orbrpg.OrbRPG;

import java.util.logging.Level;

public class FileM {
    FileM() { throw new IllegalStateException("Utility class"); }
    public static String getMessage(String path) {
        var message = OrbRPG.getInstance().getLanguageFile().getString(path);
        if (message == null) {
            OrbRPG.getInstance().getLogger().log(Level.WARNING,
                    "There is a message missing in the language file! Path: {0}",
                    path.replace(".", " > "));
            return "";
        }
        return Misc.coloured(message);
    }
    public static Integer getInt(String configPath) { return OrbRPG.getInstance().getConfig().getInt(configPath); }
    public static Integer getInt(String configPath, int defaultValue) { return OrbRPG.getInstance().getConfig().getInt(configPath, defaultValue); }
    public static Double getDouble(String configPath) { return OrbRPG.getInstance().getConfig().getDouble(configPath); }
    public static Double getDouble(String configPath, double defaultValue) { return OrbRPG.getInstance().getConfig().getDouble(configPath, defaultValue); }
    public static Float getFloat(String configPath) { return (float) (OrbRPG.getInstance().getConfig().getDouble(configPath)); }
    public static Float getFloat(String configPath, float defaultValue) { return (float) (OrbRPG.getInstance().getConfig().getDouble(configPath, defaultValue)); }
    public static boolean getBoolean(String configPath) { return OrbRPG.getInstance().getConfig().getBoolean(configPath); }
    public static String getString(String configPath, String defaultValue) { return OrbRPG.getInstance().getConfig().getString(configPath, defaultValue); }

    // Other ways to use
    public static boolean isTrue(String configPath) { return getBoolean(configPath); }
    public static boolean isFalse(String configPath) { return !getBoolean(configPath); }
}
