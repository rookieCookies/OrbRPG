package orbrpg;

import net.milkbowl.vault.economy.Economy;
import orbrpg.commands.getitem.GetItemCommand;
import orbrpg.commands.getitem.GetItemTabComplete;
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
import java.util.logging.Logger;

public final class OrbRPG extends JavaPlugin {
    public static OrbRPG instance;
    public FileConfiguration languageFileConfiguration;
    public File languageFile;
    public FileConfiguration itemsFileConfiguration;
    public File itemsDataBaseFile;
    public FileConfiguration itemsDataBaseFileConfiguration;
    public static Economy economy;
    DecimalFormat decimalFormat;
    public Logger logger;

    public OrbRPG() {
        this.decimalFormat = new DecimalFormat("0.000");
        this.logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();
        instance = this;
        logger = this.getLogger();

        saveDefaultConfig();
        new CreateFiles();
        new RegisterItems();
        registerEvents();
        if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        Objects.requireNonNull(this.getCommand("get")).setExecutor(new GetItemCommand());
        Objects.requireNonNull(this.getCommand("get")).setTabCompleter(new GetItemTabComplete());
        final long fin = System.currentTimeMillis() - start;
        this.logger.info("OrbRPG v" + this.getDescription().getVersion() + " has been successfully enabled in " + this.decimalFormat.format(fin / 1000.0) + " seconds (" + fin + "ms)");
    }
    boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return false; }
        economy = rsp.getProvider();
        logger.info("Successfully imported economy settings using Vault!");
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
        manager.registerEvents(new PlayerJoinEventListener(), this);
    }
    public FileConfiguration getLanguageFile() { return this.languageFileConfiguration; }
    public FileConfiguration getItemsFile() { return this.itemsFileConfiguration; }
    public FileConfiguration getItemDatabase() { return this.itemsDataBaseFileConfiguration; }
    public void saveItemDatabase() {
        try { getItemDatabase().save(itemsDataBaseFile); }
        catch (IOException e) { e.printStackTrace(); }
    }
    public static Economy getEconomy() { return economy; }
    public static OrbRPG getInstance() { return instance; }
}