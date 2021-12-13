package orbrpg;

import net.milkbowl.vault.economy.Economy;
import orbrpg.commands.getitem.GetItemCommand;
import orbrpg.commands.getitem.GetItemTabComplete;
import orbrpg.commands.warp.WarpCommand;
import orbrpg.commands.warp.WarpTabComplete;
import orbrpg.events.*;
import orbrpg.functions.RegisterItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class OrbRPG extends JavaPlugin {
    private static OrbRPG instance;
    private FileConfiguration languageFileConfiguration;
    private File languageFile;
    private FileConfiguration itemsFileConfiguration;
    private File itemsFile;
    private FileConfiguration itemsDataBaseFileConfiguration;
    private File itemsDataBaseFile;
    private Economy economy;
    private final Logger pluginLogger;
    private final Random random = new Random();
    private final DecimalFormat decimalFormat;

    public OrbRPG() {
        this.decimalFormat = new DecimalFormat("0.000");
        this.pluginLogger = this.getLogger();
    }

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();
        instance = this;

        saveDefaultConfig();
        new CreateFiles();
        new RegisterItems();
        registerEvents();
        if (!setupEconomy() ) {
            String economySetupMessage = String.format(
                    "[%s] - Disabled due to no Vault dependency found!",
                    getDescription().getName()
            );
            getLogger().log(Level.SEVERE, economySetupMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
        Objects.requireNonNull(this.getCommand("get")).setExecutor(new GetItemCommand());
        Objects.requireNonNull(this.getCommand("get")).setTabCompleter(new GetItemTabComplete());
        Objects.requireNonNull(this.getCommand("warp")).setExecutor(new WarpCommand());
        Objects.requireNonNull(this.getCommand("warp")).setTabCompleter(new WarpTabComplete());
        final int ms = 1000;
        final long fin = System.currentTimeMillis() - start;
        String logMessage = "OrbRPG v" + this.getDescription().getVersion() + " has been successfully " +
                "enabled in " + this.decimalFormat.format(fin / ms) + " seconds (" + fin + "ms)";
        getLogger().log(Level.INFO, logMessage);
    }
    boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        economy = rsp.getProvider();
        logger().log(Level.INFO, "Successfully imported economy settings using Vault!");
        return true;
    }
    void registerEvents() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new EntityProjectileHitEventListener(), this);
        manager.registerEvents(new PlayerAttackEventListener(), this);
        manager.registerEvents(new PlayerBowEventListener(), this);
        manager.registerEvents(new PlayerJoinEventListener(), this);
        manager.registerEvents(new PlayerCraftEventListener(), this);
        manager.registerEvents(new PlayerEquipmentChangeEventListener(), this);
        manager.registerEvents(new PlayerInteractEventListenerListener(), this);
        manager.registerEvents(new PlayerItemDropEventListener(), this);
    }
    public FileConfiguration getLanguageFile() { return this.languageFileConfiguration; }
    public void setLanguageFileConfiguration(FileConfiguration fileConfiguration) {
        languageFileConfiguration = fileConfiguration;
    }
    public File getMainLanguageFile() { return this.languageFile; }
    public void setMainLanguageFile(File file) {
        languageFile = file;
    }

    public FileConfiguration getItemsFile() { return this.itemsFileConfiguration; }
    public void setItemsFileConfiguration(FileConfiguration fileConfiguration) {
        itemsFileConfiguration = fileConfiguration;
    }
    public File getMainItemsFile() { return this.itemsFile; }
    public void setMainItemsFile(File file) {
        itemsFile = file;
    }

    public FileConfiguration getItemDatabase() { return this.itemsDataBaseFileConfiguration; }
    public void setItemsDataBaseFileConfiguration(FileConfiguration fileConfiguration) {
        itemsDataBaseFileConfiguration = fileConfiguration;
    }
    public File getMainItemDatabaseFile() { return this.itemsDataBaseFile; }
    public void setMainItemDatabaseFile(File file) {
        itemsDataBaseFile = file;
    }
    public void saveItemDatabase() {
        try {
            getItemDatabase().save(itemsDataBaseFile);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Random getRand() { return random; }
    public Logger logger() { return pluginLogger; }
    public Economy getEconomy() { return economy; }
    public static OrbRPG getInstance() { return instance; }
}
