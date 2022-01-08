package utils;

import orbrpg.OrbRPG;
import orbrpg.functions.PlayerRefreshUI;
import orbrpg.systems.LevelingSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

public class PlayerData {
    private final Player p;
    private final OrbRPG i;

    public PlayerData(Player player) {
        this.p = player;
        this.i = OrbRPG.getInstance();
    }
    public PersistentDataContainer getPersistentDataStorage() {
        return p.getPersistentDataContainer();
    }
    private void setFloat(String nameSpace, float value) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.FLOAT, value);
    }
    private Float getFloat(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        Float returnValue = container.get(nsk, PersistentDataType.FLOAT);
        if (returnValue == null)
            returnValue = 0F;
        return returnValue;
    }
    private void setInt(String nameSpace, int value) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, value);
    }
    private Integer getInt(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        Integer returnValue = container.get(nsk, PersistentDataType.INTEGER);
        if (returnValue == null)
            returnValue = 0;
        return returnValue;
    }
    private void setTrue(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, 1);
    }
    private void setFalse(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, 0);
    }
    private boolean isTrue(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        var nsk = new NamespacedKey(i, nameSpace);
        Integer value = container.get(nsk, PersistentDataType.INTEGER);
        if (value == null)
            value = 0;
        return value == 1;
    }
    // Add to data functions
    public void addExp(float amount) {
        setCurrentExp(getCurrentExp() + amount);
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
        new LevelingSystem(p).levelUp();
    }
    public void addHealth(float amount) {
        setCurrentHealth(getCurrentHealth() + amount);
        new PlayerRefreshUI(p);
    }
    public void addDarkQuartz(float amount) {
        setDarkQuartz(getDarkQuartz() + amount);
        new PlayerRefreshUI(p);
    }

    // Set data functions
    public void setDamage(float value) { setFloat(getDamageID(), value); }
    public void setCurrentHealth(float value) {
        if (value > getMaximumHealth())
            value = getMaximumHealth();
        setFloat(getCurrentHealthID(), value);
    }
    public void setMaximumHealth(float value) { setFloat(getMaximumHealthID(), value); }
    public void setCurrentTex(float value) {
        if (value > getMaximumTex())
            value = getMaximumTex();
        setFloat(getCurrentTexID(), value);
    }
    public void setDarkQuartz(float value) { setFloat(getDarkQuartzID(), value); }
    public void setMaximumTex(float value) { setFloat(getMaximumTexID(), value); }
    public void setDefense(float value) { setFloat(getDefenseID(), value); }
    public void setSpeed(float value) { setFloat(getSpeedID(), value); }
    public void setLifeSteal(float value) { setFloat(getLifeStealID(), value); }

    public void setCurrentExp(float value) { setFloat(getCurrentExpID(), value); }
    public void setLevel(int value) { setInt(getLevelID(), value); }
    public void setMaximumExp(float value) { setFloat(getMaximumExpID(), value); }

    // Cooldown
    public void setAttackCooldownTrue() { setTrue(getAttackCooldownID()); }
    public void setAttackCooldownFalse() { setFalse(getAttackCooldownID()); }
    public void setBowCooldownTrue() { setTrue(getBowCooldownID()); }
    public void setBowCooldownFalse() { setFalse(getBowCooldownID()); }
    public void setCrystalOneCooldownTrue() { setTrue(getCrystalOneCooldownID()); }
    public void setCrystalOneCooldownFalse() { setFalse(getCrystalOneCooldownID()); }
    public void setCrystalTwoCooldownTrue() { setTrue(getCrystalTwoCooldownID()); }
    public void setCrystalTwoCooldownFalse() { setFalse(getCrystalTwoCooldownID()); }

    // Get data functions
    public float getDamage() { return getFloat(getDamageID()); }
    public float getCurrentHealth() { return getFloat(getCurrentHealthID()); }
    public float getMaximumHealth() { return getFloat(getMaximumHealthID()); }
    public float getDarkQuartz() { return getFloat(getDarkQuartzID()); }
    public float getCurrentTex() { return getFloat(getCurrentTexID()); }
    public float getMaximumTex() { return getFloat(getMaximumTexID()); }
    public float getDefense() { return getFloat(getDefenseID()); }
    public float getSpeed() { return getFloat(getSpeedID()); }
    public float getLifeSteal() { return getFloat(getLifeStealID()); }

    public float getCurrentExp() { return getFloat(getCurrentExpID()); }
    public float getMaximumExp() { return getFloat(getMaximumExpID()); }
    public int getLevel() { return getInt(getLevelID()); }

    public String getCrystalOneID() {
        PersistentDataContainer data = Item.getDataOfItem(getCrystalOne());
        String returnValue = data.get(new NamespacedKey(OrbRPG.getInstance(), "crystal_id"), PersistentDataType.STRING);
        if (returnValue == null)
            returnValue = "";
        return returnValue;
    }
    public String getCrystalTwoID() {
        PersistentDataContainer data = Item.getDataOfItem(getCrystalTwo());
        String returnValue = data.get(new NamespacedKey(OrbRPG.getInstance(), "crystal_id"), PersistentDataType.STRING);
        if (returnValue == null)
            returnValue = "";
        return returnValue;
    }
    public ItemStack getCrystalOne() { return p.getInventory().getItem(9); }
    public ItemStack getCrystalTwo() { return p.getInventory().getItem(10); }

    // Is functions
    public boolean isAttackCooldownTrue() { return isTrue(getAttackCooldownID()); }
    public boolean isBowCooldownTrue() { return isTrue(getBowCooldownID()); }
    public boolean isCrystalOneCooldownTrue() { return isTrue(getCrystalOneCooldownID()); }
    public boolean isCrystalTwoCooldownTrue() { return isTrue(getCrystalTwoCooldownID()); }

    // Get data ID functions
    private String getDamageID() { return getDataID("data_ids.player.stats.damage"); }
    private String getCurrentHealthID() { return getDataID("data_ids.player.stats.maximum_health"); }
    private String getMaximumHealthID() { return getDataID("data_ids.player.stats.current_health"); }
    private String getDarkQuartzID() { return getDataID("data_ids.player.stats.dark_quartz"); }
    private String getCurrentTexID() { return getDataID("data_ids.player.stats.maximum_tex"); }
    private String getMaximumTexID() { return getDataID("data_ids.player.stats.current_tex"); }
    private String getDefenseID() { return getDataID("data_ids.player.stats.defense"); }
    private String getSpeedID() { return getDataID("data_ids.player.stats.speed"); }
    private String getLifeStealID() { return getDataID("data_ids.player.stats.life_steal"); }

    private String getLevelID() { return getDataID("data_ids.player.stats.leveling.level"); }
    private String getCurrentExpID() { return getDataID("data_ids.player.stats.leveling.current_exp"); }
    private String getMaximumExpID() { return getDataID("data_ids.player.stats.leveling.maximum_exp"); }

    private String getAttackCooldownID() { return getDataID("data_ids.player.cooldowns.attack_cooldown"); }
    private String getBowCooldownID() { return getDataID("data_ids.player.cooldowns.bow_cooldown"); }
    private String getCrystalOneCooldownID() { return getDataID("data_ids.player.cooldowns.crystal_one"); }
    private String getCrystalTwoCooldownID() { return getDataID("data_ids.player.cooldowns.crystal_two"); }

    // General functions
    private String getDataID(String path) {
        var message = i.getConfig().getString(path);
        if (message == null) {
            i.getLogger().log(Level.WARNING,
                    "There is a data ID missing in the config file! Path: {0}",
                    path.replace(".", " > "));
            return "";
        }
        return message;
    }
    public void resetCooldown() {
        setCrystalOneCooldownFalse();
        setCrystalTwoCooldownFalse();
        setAttackCooldownFalse();
        setBowCooldownFalse();
    }
}
