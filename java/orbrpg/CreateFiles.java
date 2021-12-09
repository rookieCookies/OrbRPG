package orbrpg;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CreateFiles {
    OrbRPG instance = OrbRPG.getInstance();
    CreateFiles() {
        createLanguageFile();
        createItemsFile();
        createItemDataBase();
    }

    void createLanguageFile() {
        String languageFilePath = instance.getConfig().getString("language_file");
        if (languageFilePath == null) {
            instance.getConfig().set("language_file", "lang_default");
            languageFilePath = instance.getConfig().getString("language_file");
            instance.saveConfig();
        }
        languageFilePath += ".yml";
        instance.logger.info(languageFilePath);
        File languageFile = new File(instance.getDataFolder(), languageFilePath);
        if (!languageFile.exists()) {
            instance.saveResource("default.yml", false);
            File defaultFile = new File(instance.getDataFolder(), "default.yml");
            boolean f = defaultFile.renameTo(languageFile);
            if (!f) {
                createLanguageFile();
                return;
            }
        }
        instance.languageFileConfiguration = new YamlConfiguration();
        try {
            instance.languageFileConfiguration.load(languageFile);
            instance.getLogger().info("Successfully loaded the language file! (If a message is in the language file it will throw an error when that message is needed!)");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createItemsFile() {
        String filePath = instance.getConfig().getString("item_file");
        if (filePath == null) {
            instance.getConfig().set("item_file", "items");
            filePath = instance.getConfig().getString("item_file");
            instance.saveConfig();
        }
        filePath += ".yml";
        instance.logger.info(filePath);
        File languageFile = new File(instance.getDataFolder(), filePath);
        if (!languageFile.exists()) {
            instance.saveResource("items.yml", false);
            File defaultFile = new File(instance.getDataFolder(), "items.yml");
            boolean f = defaultFile.renameTo(languageFile);
            if (!f) {
                createItemsFile();
                return;
            }
        }
        instance.itemsFileConfiguration = new YamlConfiguration();
        try {
            instance.itemsFileConfiguration.load(languageFile);
            instance.getLogger().info("Successfully loaded the items file!");
        }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createItemDataBase() {
        String filePath = instance.getConfig().getString("items_database_file");
        if (filePath == null) {
            instance.getConfig().set("items_database_file", "items_database");
            filePath = instance.getConfig().getString("items_database_file");
            instance.saveConfig();
        }
        filePath += ".yml";
        instance.logger.info(filePath);
        File languageFile = new File(instance.getDataFolder(), filePath);
        if (languageFile.exists()) {
            languageFile.delete();
        }
        instance.saveResource("items_database.yml", false);
        File defaultFile = new File(instance.getDataFolder(), "items_database.yml");
        boolean f = defaultFile.renameTo(languageFile);
        if (!f) {
            createLanguageFile();
            return;
        }
        instance.itemsDataBaseFile = languageFile;
        instance.itemsDataBaseFileConfiguration = new YamlConfiguration();
        try {
            instance.itemsDataBaseFileConfiguration.load(languageFile);
            instance.getLogger().info("Successfully loaded items database!");
        }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
}
