package orbrpg.events.mining;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import utils.Item;
import utils.PlayerData;

public class MiningWheat implements Listener {
    @EventHandler
    public void onMineWheat(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        if (e.getBlock().getType() != Material.WHEAT)
            return;
        Ageable age = (Ageable) e.getBlock().getBlockData();
        if (age.getAge() == 0)
            return;
        var chance = 5;
        var exp = Math.random() * 3;
        ItemStack drop = Item.getItem("seeds");
        Ageable crop = (Ageable) e.getBlock().getBlockData();
        if (crop.getAge() == 7)
            chance = 75;
        if (Math.random() * 100 <= chance)
            drop = Item.getItem("wheat");
        var dropLoc = e.getBlock().getLocation();
        dropLoc.setY(dropLoc.getY() + 0.5);
        var data = new PlayerData(e.getPlayer());
        if (Math.random() * 100 < chance) {
            data.addExp((float) exp);
            drop.setAmount(1);
            dropLoc.getWorld().dropItem(dropLoc, drop);
        }
        var timeOne = 5;
        var timeTwo = timeOne + Math.round(Math.random() * 40 / 20);
        var timeThree = timeTwo + Math.round(Math.random() * 50 / 20);
        var timeFour = timeThree + Math.round(Math.random() * 55 / 20);
        var timeFive = timeFour + Math.round(Math.random() * 60 / 20);
        var timeSix = timeFive + Math.round(Math.random() * 70 / 20);
        var timeSeven = timeSix + Math.round(Math.random() * 120 / 20);
        e.getBlock().setType(Material.WHEAT);
        age.setAge(0);
        e.getBlock().setBlockData(age);
        updateStage(e.getBlock(), timeOne, 0);
        updateStage(e.getBlock(), timeTwo, 1);
        updateStage(e.getBlock(), timeThree, 2);
        updateStage(e.getBlock(), timeFour, 3);
        updateStage(e.getBlock(), timeFive, 4);
        updateStage(e.getBlock(), timeSix, 5);
        updateStage(e.getBlock(), timeSeven, 6);
    }
    public void updateStage(@NotNull Block block, long time, int oldAge) {
        var blockLoc = block.getLocation();
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> {
            var b = blockLoc.getBlock();
            if (b.getType() == Material.AIR && oldAge != 0)
                return;
            Ageable age = (Ageable) b.getBlockData();
            if (age.getAge() != oldAge)
                return;
            age.setAge(age.getAge() + 1);
            b.setBlockData(age);
        }, 20L * time);
    }
}
