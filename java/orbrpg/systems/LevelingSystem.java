package orbrpg.systems;

import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LevelingSystem {
    Player p;
    PlayerData data;
    public LevelingSystem(Player p) {
        this.p = p;
        this.data = new PlayerData(p);
    }
    public void levelUp() {
        float currentExp = data.getFloat("current_exp");
        float maximumExp = data.getFloat("maximum_exp");
        int currentLevel = data.getInt("current_level");
        p.sendMessage("Current EXP: " + currentExp + " REQUIRED: " + maximumExp + " LEVEL: " + currentLevel);
        if (currentExp < maximumExp) return;
        currentLevel++;
        currentExp -= maximumExp;
        maximumExp = getEXPRequired(currentLevel);
        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 3F, 0.5F);
        p.sendMessage(Misc.coloured("&6&l---------------------------"));
        p.sendMessage(Misc.coloured(""));
        p.sendMessage(Misc.coloured("&e&l LEVEL UP! &8%level_1% &3» &b&l%level_2%".
                replaceAll("%level_1%", Misc.toRoman(currentLevel - 1)
                        .replaceAll("%level_2%", Misc.toRoman(currentLevel)))));
        p.sendMessage(Misc.coloured(""));
        p.sendMessage(Misc.coloured("&a&l REWARDS:"));
        p.sendMessage(Misc.coloured("&6&l---------------------------"));
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (on == p) continue;
            on.sendMessage(Misc.getMessage("messages.level_up")
                    .replaceAll("%old_level%", Misc.toRoman(currentLevel - 1)
                            .replaceAll("%level%", Misc.toRoman(currentLevel))
                            .replaceAll("%player%", p.getName()))
            );
        }
    }
    public float getEXPRequired(float level) {
        level++;
        float baseExp = (float) OrbRPG.getInstance().getConfig().getDouble("base_stats.exp");
        return Math.round(baseExp * Math.pow(level, 3) / 5 + 10);
    }
}
