package orbrpg.systems;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import utils.FileM;
import utils.Misc;
import utils.i.NumberM;

import java.util.logging.Level;

public class LevelingSystem {
    private final Player p;
    private final PlayerData data;
    public LevelingSystem(Player p) {
        this.p = p;
        this.data = new PlayerData(p);
    }
    public void levelUp() {
        float currentExp = data.getCurrentExp();
        float maximumExp = data.getMaximumExp();
        if (currentExp < maximumExp)
            return;
        int currentLevel = data.getLevel();
        currentLevel++;
        currentExp -= maximumExp;
        maximumExp = getEXPRequired(currentLevel);
        p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 3F, 0.5F);
        p.sendMessage(Misc.coloured("&6&l---------------------------"));
        p.sendMessage(Misc.coloured(""));
        p.sendMessage(Misc.coloured("&e&l LEVEL UP! &8%level_1% &3» &b&l%level_2%"
                .replace("%level_1%", NumberM.toRoman(currentLevel - 1))
                .replace("%level_2%", NumberM.toRoman(currentLevel))));
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
            on.sendMessage(FileM.getMessage("messages.level_up")
                    .replace("%old_level%", NumberM.toRoman(currentLevel - 1))
                    .replace("%level%", NumberM.toRoman(currentLevel))
                    .replace("%player%", p.getName())
            );
        }
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.systems.leveling_system.level_up"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Systems > " + getClass().getName() + " > levelUp",
                    p.getName()
            );
    }
    public float getEXPRequired(float level) {
        level++;
        float baseExp = (float) OrbRPG.getInstance().getConfig().getDouble("stats.base_stats.exp");
        final long round = Math.round(baseExp * Math.pow(level, 3) / 5 + 10);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.systems.leveling_system.get_exp"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Systems > " + getClass().getName() + " > getEXPRequired > " + round,
                    p.getName()
            );
        return round;
    }
}
