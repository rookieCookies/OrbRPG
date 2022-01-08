package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import utils.Misc;
import utils.PlayerData;

import java.util.logging.Level;

public class Scoreboard {
    private final Player p;

    public Scoreboard(Player p) {
        this.p = p;
        run();
    }

    public void run() {
        var manager = Bukkit.getScoreboardManager();
        var board = manager.getNewScoreboard();
        var obj = board.registerNewObjective("Scoreboard", "dummy", "Loading...");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        var separator = board.registerNewTeam("separator");
        var balance = board.registerNewTeam("balance");
        var darkQuartz = board.registerNewTeam("dark_quartz");
        var level = board.registerNewTeam("level");
        var separatorText = "";
        String balanceTxt = Misc.getMessage("scoreboard.balance");
        String darkQuartzTxt = Misc.getMessage("scoreboard.dark_quartz");
        String levelTxt = Misc.getMessage("scoreboard.level");
        separator.addEntry(separatorText);
        separator.addEntry(separatorText + ChatColor.WHITE);
        level.addEntry(levelTxt);

        var balanceText = obj.getScore(balanceTxt);
        var darkQuartzText = obj.getScore(darkQuartzTxt);
        var discordServerMessage = obj.getScore(Misc.getMessage("scoreboard.bottom_text"));
        var blankSpace1 = obj.getScore(" ");
        var blankSpace2 = obj.getScore("  ");
        var blankSpace3 = obj.getScore("   ");
        var blankSpace4 = obj.getScore("    ");
        var blankSpace5 = obj.getScore("     ");
        var separatorText1 = obj.getScore(separatorText);
        var separatorText2 = obj.getScore(separatorText + ChatColor.WHITE);
        var levelText = obj.getScore(levelTxt);
        balance.addEntry(balanceTxt);
        darkQuartz.addEntry(darkQuartzTxt);
        discordServerMessage.setScore(0);
        blankSpace1.setScore(1);
        separatorText1.setScore(2);
        blankSpace2.setScore(3);
        darkQuartzText.setScore(4);
        balanceText.setScore(5);
        blankSpace3.setScore(6);
        separatorText2.setScore(7);
        blankSpace4.setScore(8);
        levelText.setScore(9);
        blankSpace5.setScore(10);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Bukkit.getOnlinePlayers().contains(p))
                    cancel();
                var data = new PlayerData(p);
                ConfigurationSection config = OrbRPG.getInstance().getConfig();
                long time = p.getWorld().getTime();
                balance.suffix(Component.text(Misc.formatNumber((float) OrbRPG.getInstance().getEconomy().getBalance(p))));
                darkQuartz.suffix(Component.text(ChatColor.LIGHT_PURPLE + Misc.formatNumber(data.getDarkQuartz())));
                var levelInt = data.getLevel();
                int levelNext = levelInt + 1;
                level.suffix(Component.text(Misc.coloured("&a" + Misc.formatNumber(levelInt) +
                        " &7» &8" + Misc.formatNumber(levelNext) +
                        " &7(" + Misc.formatNumber(data.getCurrentExp() / data.getMaximumExp() * 100) + "&7%)")));
                var line = "";
                if (config.getInt("times.sun_rise.start") < time && time < config.getInt("times.sun_rise.end")) {
                    line = Misc.getMessage("scoreboard.time_based.separator.sun_rise");
                    obj.displayName(Component.text(Misc.getMessage("scoreboard.time_based.title.sun_rise")));
                }
                else if (config.getInt("times.day.start") < time && time < config.getInt("times.day.end")) {
                    line = Misc.getMessage("scoreboard.time_based.separator.day");
                    obj.displayName(Component.text(Misc.getMessage("scoreboard.time_based.title.day")));
                }
                else if (config.getInt("times.sun_set.start") < time && time < config.getInt("times.sun_set.end")) {
                    line = Misc.getMessage("scoreboard.time_based.separator.sun_set");
                    obj.displayName(Component.text(Misc.getMessage("scoreboard.time_based.title.sun_set")));
                }
                else if (config.getInt("times.night.start") < time && time < config.getInt("times.night.end")) {
                    line = Misc.getMessage("scoreboard.time_based.separator.night");
                    obj.displayName(Component.text(Misc.getMessage("scoreboard.time_based.title.night")));
                } else
                    p.getWorld().setFullTime(config.getInt("times.sun_rise.start"));
                separator.suffix(Component.text(line));
            }
        }.runTaskTimer(OrbRPG.getInstance(), 3, 10);
        p.setScoreboard(board);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.register_scoreboard"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}