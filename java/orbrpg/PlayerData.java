package orbrpg;

import orbrpg.systems.LevelingSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class PlayerData {
    private final Player p;

    public PlayerData(Player player) {
        this.p = player;
    }
    public PersistentDataContainer getPersistentDataStorage() {
        return p.getPersistentDataContainer();
    }
    public boolean setFloat(String nameSpace, float value) {
        PersistentDataContainer container = getPersistentDataStorage();
        container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT, value);
        return true;
    }
    public Float getFloat(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        Float returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT);
        if (returnValue == null) returnValue = 0f;
        return returnValue;
    }
    public boolean setInt(String nameSpace, int value) {
        PersistentDataContainer container = getPersistentDataStorage();
        container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.INTEGER, value);
        return true;
    }
    public Integer getInt(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        Integer returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.INTEGER);
        if (returnValue == null) returnValue = 0;
        return returnValue;
    }
    public boolean setDouble(String nameSpace, double value) {
        PersistentDataContainer container = getPersistentDataStorage();
        container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.DOUBLE, value);
        return true;
    }
    public Double getDouble(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        Double returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.DOUBLE);
        if (returnValue == null) returnValue = 0.0;
        return returnValue;
    }
    public boolean setString(String nameSpace, String value) {
        PersistentDataContainer container = getPersistentDataStorage();
        container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.STRING, value);
        return true;
    }
    public String getString(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        String returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.STRING);
        if (returnValue == null) returnValue = "";
        return returnValue;
    }
    public boolean setBool(String nameSpace, boolean value) {
        PersistentDataContainer container = getPersistentDataStorage();
        if (value) container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.INTEGER, 1);
        else container.set(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.INTEGER, 0);
        return true;
    }
    public boolean getBool(String nameSpace) {
        PersistentDataContainer container = getPersistentDataStorage();
        int value = 0;
        Object o = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.INTEGER);
        if (o != null)
            value = (int) o;
        System.out.println(value == 1);
        return value == 1;
    }
    public void addExp(float amount) {
        if (amount < 0) amount = Math.abs(amount);
        float currentExp = getFloat("current_exp");
        float changedExp = currentExp + amount;
        setFloat("current_exp", changedExp);
        p.sendMessage("Old Current: " + currentExp + " Changed: " + changedExp + " Current: " + getFloat("current_exp"));
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 1f);
        new LevelingSystem(p).levelUp();
    }
}
