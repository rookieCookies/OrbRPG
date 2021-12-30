package utils;

import orbrpg.OrbRPG;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class CreateFiles {
    private final OrbRPG instance = OrbRPG.getInstance();
    public CreateFiles() {
        createLanguageFile();
        createItemsFile();
        createBlocksFile();
        createRecipesFile();
        createItemDataBase();
    }

    void createLanguageFile() {
        var languageFilePathInConfig = "language_file";
        var languageFilePath = instance.getConfig().getString(languageFilePathInConfig);
        if (languageFilePath == null) {
            instance.getConfig().set(languageFilePathInConfig, "lang_default");
            languageFilePath = instance.getConfig().getString(languageFilePathInConfig);
            instance.saveConfig();
        }
        languageFilePath += ".yml";
        var languageFile = new File(instance.getDataFolder(), languageFilePath);
        if (!languageFile.exists()) {
            instance.saveResource("default.yml", false);
            var defaultFile = new File(instance.getDataFolder(), "default.yml");
            boolean f = defaultFile.renameTo(languageFile);
            if (!f) {
                createLanguageFile();
                return;
            }
        }
        instance.setLanguageFileConfiguration(new YamlConfiguration());
        try {
            instance.getLanguageFile().load(languageFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the language file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
        instance.setMainLanguageFile(languageFile);
    }
    void createItemsFile() {
        var itemsFilePathInConfig = "items_file";
        var filePath = instance.getConfig().getString(itemsFilePathInConfig);
        if (filePath == null) {
            instance.getConfig().set(itemsFilePathInConfig, "items");
            filePath = instance.getConfig().getString(itemsFilePathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var itemsFile = new File(instance.getDataFolder(), filePath);
        if (!itemsFile.exists()) {
            instance.saveResource("items.yml", false);
            var defaultFile = new File(instance.getDataFolder(), "items.yml");
            boolean f = defaultFile.renameTo(itemsFile);
            if (!f) {
                createItemsFile();
                return;
            }
        }
        instance.setItemsFileConfiguration(new YamlConfiguration());
        try {
            instance.getItemsFile().load(itemsFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the items file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createBlocksFile() {
        var blocksFilePathInConfig = "blocks_file";
        var filePath = instance.getConfig().getString(blocksFilePathInConfig);
        if (filePath == null) {
            instance.getConfig().set(blocksFilePathInConfig, "blocks");
            filePath = instance.getConfig().getString(blocksFilePathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var itemsFile = new File(instance.getDataFolder(), filePath);
        if (!itemsFile.exists()) {
            instance.saveResource("blocks.yml", false);
            var defaultFile = new File(instance.getDataFolder(), "blocks.yml");
            boolean f = defaultFile.renameTo(itemsFile);
            if (!f) {
                createBlocksFile();
                return;
            }
        }
        instance.setBlocksFileConfiguration(new YamlConfiguration());
        try {
            instance.getBlocksFile().load(itemsFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the blocks file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createRecipesFile() {
        var recipesFilePathInConfig = "recipes_file";
        var filePath = instance.getConfig().getString(recipesFilePathInConfig);
        if (filePath == null) {
            instance.getConfig().set(recipesFilePathInConfig, "recipes");
            filePath = instance.getConfig().getString(recipesFilePathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var itemsFile = new File(instance.getDataFolder(), filePath);
        if (!itemsFile.exists()) {
            instance.saveResource("recipes.yml", false);
            var defaultFile = new File(instance.getDataFolder(), "recipes.yml");
            boolean f = defaultFile.renameTo(itemsFile);
            if (!f) {
                createBlocksFile();
                return;
            }
        }
        instance.setRecipesFileConfiguration(new YamlConfiguration());
        try {
            instance.getRecipesFile().load(itemsFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the recipes file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createItemDataBase() {
        var itemsDataBasePathInConfig = "items_database_file";
        var filePath = instance.getConfig().getString(itemsDataBasePathInConfig);
        if (filePath == null) {
            instance.getConfig().set(itemsDataBasePathInConfig, "items_database");
            filePath = instance.getConfig().getString(itemsDataBasePathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var databaseFile = new File(instance.getDataFolder(), filePath);
        if (databaseFile.exists() && !databaseFile.delete()) {
            createItemDataBase();
            return;
        }
        instance.saveResource("items_database.yml", false);
        var defaultFile = new File(instance.getDataFolder(), "items_database.yml");
        boolean f = defaultFile.renameTo(databaseFile);
        if (!f) {
            createLanguageFile();
            return;
        }
        instance.setMainItemDatabaseFile(databaseFile);
        instance.setItemsDataBaseFileConfiguration(new YamlConfiguration());
        try {
            instance.getItemDatabase().load(databaseFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded items database!");
        }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
}
