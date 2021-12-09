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

public class Scoreboard {
    private final Player p;

    public Scoreboard(Player p) {
        this.p = p;
        run();
    }

    public void run() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("Scoreboard", "dummy", "Loading...");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team separator = board.registerNewTeam("separator");
        Team balance = board.registerNewTeam("balance");
        Team level = board.registerNewTeam("level");
        String separatorText = "";
        String balanceTxt = Misc.getMessage("scoreboard.balance");
        String levelTxt = Misc.getMessage("scoreboard.level");
        separator.addEntry(separatorText);
        separator.addEntry(separatorText + ChatColor.WHITE);
        level.addEntry(levelTxt);

        Score balanceText = obj.getScore(balanceTxt);
        Score discordServerMessage = obj.getScore(Misc.getMessage("scoreboard.bottom_text"));
        Score blankSpace1 = obj.getScore(" ");
        Score blankSpace2 = obj.getScore("  ");
        Score blankSpace3 = obj.getScore("   ");
        Score blankSpace4 = obj.getScore("    ");
        Score blankSpace5 = obj.getScore("     ");
        Score separatorText1 = obj.getScore(separatorText);
        Score separatorText2 = obj.getScore(separatorText + ChatColor.WHITE);
        Score levelText = obj.getScore(levelTxt);
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
                if (!Bukkit.getOnlinePlayers().contains(p)) cancel();
                PlayerData data = new PlayerData(p);
                ConfigurationSection config = OrbRPG.getInstance().getConfig();
                long time = p.getWorld().getTime();
                balance.suffix(Component.text(String.valueOf(OrbRPG.getEconomy().getBalance(p))));
                level.suffix(Component.text(Misc.coloured("&a" + Misc.formatNumber(data.getInt("level")) + " &7» &8" + Misc.formatNumber(data.getInt("level") + 1) + " &7(" + data.getFloat("current_exp") / data.getFloat("required_exp") + "&7%)")));
                String line = "";
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
                }
                separator.suffix(Component.text(line));
            }
        }.runTaskTimer(OrbRPG.getInstance(), 0, 10);
        p.setScoreboard(board);
    }
}
