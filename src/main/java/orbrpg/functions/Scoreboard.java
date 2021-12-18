package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

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
        var level = board.registerNewTeam("level");
        var separatorText = "";
        String balanceTxt = Misc.getMessage("scoreboard.balance");
        String levelTxt = Misc.getMessage("scoreboard.level");
        separator.addEntry(separatorText);
        separator.addEntry(separatorText + ChatColor.WHITE);
        level.addEntry(levelTxt);

        var balanceText = obj.getScore(balanceTxt);
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
        discordServerMessage.setScore(0);
        blankSpace1.setScore(1);
        separatorText1.setScore(2);
        blankSpace2.setScore(3);
        balanceText.setScore(4);
        blankSpace3.setScore(5);
        separatorText2.setScore(6);
        blankSpace4.setScore(7);
        levelText.setScore(8);
        blankSpace5.setScore(9);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Bukkit.getOnlinePlayers().contains(p))
                    cancel();
                var data = new PlayerData(p);
                ConfigurationSection config = OrbRPG.getInstance().getConfig();
                long time = p.getWorld().getTime();
                balance.suffix(Component.text(String.valueOf(OrbRPG.getInstance().getEconomy().getBalance(p))));
                var levelInt = data.getLevel();
                int levelNext = levelInt + 1;
                level.suffix(Component.text(Misc.coloured("&a" + Misc.formatNumber(levelInt) +
                        " &7» &8" + Misc.formatNumber(levelNext) +
                        " &7(" + Math.round(data.getCurrentExp() / data.getMaximumExp() * 10000) / 100 + "&7%)")));
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
                    // The time spans are editable in the config.yml file,
                    // so when the night ends or an invalid time gets entered
                    // there is not much we can do other than reset the time
                    p.getWorld().setFullTime(config.getInt("times.sun_rise.start"));

                separator.suffix(Component.text(line));
            }
        }.runTaskTimer(OrbRPG.getInstance(), 0, 10);
        p.setScoreboard(board);

        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.register_scoreboard"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}
