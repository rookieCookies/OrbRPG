package orbrpg;

import orbrpg.systems.LevelingSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    public void setFloat(String nameSpace, float value) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.FLOAT, value);
    }
    public Float getFloat(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        Float returnValue = container.get(nsk, PersistentDataType.FLOAT);
        if (returnValue == null)
            returnValue = 0F;
        return returnValue;
    }
    private void setInt(String nameSpace, int value) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, value);
    }
    private Integer getInt(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        Integer returnValue = container.get(nsk, PersistentDataType.INTEGER);
        if (returnValue == null)
            returnValue = 0;
        return returnValue;
    }
    private void setTrue(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, 1);
    }
    private void setFalse(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        container.set(nsk, PersistentDataType.INTEGER, 0);
    }
    private boolean isTrue(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        NamespacedKey nsk = new NamespacedKey(i, nameSpace);
        Object o = container.get(nsk, PersistentDataType.INTEGER);
        int value = 0;
        if (o != null)
            value = (int) o;
        return value == 1;
    }
    // Add to data functions
    public void addExp(float amount) {
        setCurrentExp(getCurrentExp() + amount);
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
        new LevelingSystem(p).levelUp();
    }

    // Set data functions
    public void setDamage(float value) { setFloat(getDamageID(), value); }
    public void setCurrentHealth(float value) { setFloat(getCurrentHealthID(), value); }
    public void setMaximumHealth(float value) { setFloat(getMaximumHealthID(), value); }
    public void setCurrentTex(float value) { setFloat(getCurrentTexID(), value); }
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
    public void setBowCooldownTrue() { setTrue(getAttackCooldownID()); }
    public void setBowCooldownFalse() { setFalse(getAttackCooldownID()); }

    // Get data functions
    public float getDamage() { return getFloat(getDamageID()); }
    public float getCurrentHealth() { return getFloat(getCurrentHealthID()); }
    public float getMaximumHealth() { return getFloat(getMaximumHealthID()); }
    public float getCurrentTex() { return getFloat(getCurrentTexID()); }
    public float getMaximumTex() { return getFloat(getMaximumTexID()); }
    public float getDefense() { return getFloat(getDefenseID()); }
    public float getSpeed() { return getFloat(getSpeedID()); }
    public float getLifeSteal() { return getFloat(getLifeStealID()); }

    public float getCurrentExp() { return getFloat(getCurrentExpID()); }
    public float getMaximumExp() { return getFloat(getMaximumExpID()); }
    public int getLevel() { return getInt(getLevelID()); }

    // Is functions
    public boolean isAttackCooldownTrue() { return isTrue(getAttackCooldownID()); }
    public boolean isBowCooldownTrue() { return isTrue(getBowCooldownID()); }

    // Get data ID functions
    private String getDamageID() { return getDataID("data_ids.player.stats.damage"); }
    private String getCurrentHealthID() { return getDataID("data_ids.player.stats.maximum_health"); }
    private String getMaximumHealthID() { return getDataID("data_ids.player.stats.current_health"); }
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

    // General functions
    private String getDataID(String path) {
        String message = i.getConfig().getString(path);
        if (message == null) {
            i.getLogger().log(Level.WARNING,
                    "There is a data ID missing in the config file! Path: {0}",
                    path.replace(".", " > "));
            return "";
        }
        return message;
    }
}
