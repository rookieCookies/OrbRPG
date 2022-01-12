package orbrpg;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class CreateFiles {
    private final OrbRPG instance = OrbRPG.getInstance();
    public CreateFiles() {
        final long start = System.currentTimeMillis();
        createLanguageFile();
        createItemsFile();
        createBlocksFile();
        createRecipesFile();
        createMobsFile();
        createItemDataBase();
        final long fin = System.currentTimeMillis() - start;
        String logMessage = "All files have been generated and registered successfully! (" + fin + "ms)";
        OrbRPG.getInstance().getLogger().log(Level.INFO, logMessage);
    }

    void createLanguageFile() {
        var languageFile = createFile("language_file", "default");
        instance.setLanguageFileConfiguration(new YamlConfiguration());
        try {
            instance.getLanguageFile().load(languageFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the language file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createItemsFile() {
        var itemsFile = createFile("items_file", "items");
        instance.setItemsFileConfiguration(new YamlConfiguration());
        try {
            instance.getItemsFile().load(itemsFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the items file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createBlocksFile() {
        var blocksFile = createFile("blocks_file", "blocks");
        instance.setBlocksFileConfiguration(new YamlConfiguration());
        try {
            instance.getBlocksFile().load(blocksFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the blocks file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createRecipesFile() {
        var recipesFile = createFile("recipes_file", "recipes");
        instance.setRecipesFileConfiguration(new YamlConfiguration());
        try {
            instance.getRecipesFile().load(recipesFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the recipes file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createMobsFile() {
        var mobsFile = createFile("mobs_file", "mobs");
        instance.setMainMobsFile(mobsFile);
        instance.setMobsFileConfiguration(new YamlConfiguration());
        try {
            instance.getMobsFile().load(mobsFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the mobs file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    void createItemDataBase() {
        var databaseFile = createFile("items_database_file", "items_database");
        instance.setMainItemDatabaseFile(databaseFile);
        instance.setItemsDataBaseFileConfiguration(new YamlConfiguration());
        try {
            instance.getItemDatabase().load(databaseFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded items database!");
        }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }
    File createFile(String pathInConfig, String fileName) {
        pathInConfig = "file_management." + pathInConfig;
        var filePath = instance.getConfig().getString(pathInConfig);
        if (filePath == null) {
            instance.getConfig().set(pathInConfig, fileName);
            filePath = instance.getConfig().getString(pathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var file = new File(instance.getDataFolder(), filePath);
        if (!file.exists()) {
            instance.saveResource(fileName + ".yml", false);
            var defaultFile = new File(instance.getDataFolder(), fileName + ".yml");
            boolean f = defaultFile.renameTo(file);
            if (!f) {
                file = createFile(pathInConfig, fileName);
            }
        }
        return file;
    }
}
