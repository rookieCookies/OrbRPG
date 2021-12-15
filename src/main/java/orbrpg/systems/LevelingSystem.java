package orbrpg.systems;

import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LevelingSystem {
    private final Player p;
    private final PlayerData data;
    public LevelingSystem(Player p) {
        this.p = p;
        this.data = new PlayerData(p);
    }
    // TODO: DOESN'T WORK AT ALL!
    public void levelUp() {
        float currentExp = data.getCurrentExp();
        float maximumExp = data.getMaximumExp();
        if (currentExp < maximumExp) {
            return;
        }
        int currentLevel = data.getLevel();
        currentLevel++;
        currentExp -= maximumExp;
        maximumExp = getEXPRequired(currentLevel);
        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 3F, 0.5F);
        p.sendMessage(Misc.coloured("&6&l---------------------------"));
        p.sendMessage(Misc.coloured(""));
        p.sendMessage(Misc.coloured("&e&l LEVEL UP! &8%level_1% &3» &b&l%level_2%"
                .replace("%level_1%", Misc.toRoman(currentLevel - 1))
                .replace("%level_2%", Misc.toRoman(currentLevel))));
        p.sendMessage(Misc.coloured(""));
        p.sendMessage(Misc.coloured("&a&l REWARDS:"));
        p.sendMessage(Misc.coloured("&6&l---------------------------"));
        data.setCurrentExp(currentExp);
        data.setMaximumExp(maximumExp);
        data.setLevel(currentLevel);

        new PlayerRefreshUI(p);
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), this::levelUp, 1);
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (on == p)
                continue;
            on.sendMessage(Misc.getMessage("messages.level_up")
                    .replace("%old_level%", Misc.toRoman(currentLevel - 1))
                    .replace("%level%", Misc.toRoman(currentLevel))
                    .replace("%player%", p.getName())
            );
        }
    }
    public float getEXPRequired(float level) {
        level++;
        float baseExp = (float) OrbRPG.getInstance().getConfig().getDouble("base_stats.exp");
        return Math.round(baseExp * Math.pow(level, 3) / 5 + 10);
    }
}
