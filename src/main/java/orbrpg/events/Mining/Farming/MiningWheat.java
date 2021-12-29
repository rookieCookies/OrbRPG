package orbrpg.events.Mining.Farming;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.jetbrains.annotations.NotNull;
import utils.PlayerData;

public class MiningWheat implements Listener {
    @EventHandler
    public void onMineWheat(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        if (e.getBlock().getType() != Material.WHEAT)
            return;
        var chance = 5;
        var exp = Math.random() * 3;
        ItemStack drop;
        var crop = (Crops) e.getBlock();
        if (crop.getState().equals(CropState.RIPE))
            chance = 75;
        if (Math.random() * 100 <= 25)
            drop = new ItemStack(Material.WHEAT, 1);
        else
            drop = new ItemStack(Material.WHEAT_SEEDS, (int) Math.round(Math.random() * 2));
        e.getBlock().setType(Material.AIR);
        var dropLoc = e.getBlock().getLocation();
        dropLoc.setY(dropLoc.getY() + 0.5);
        PlayerData data = new PlayerData(e.getPlayer());
        if (Math.random() * 100 < chance) {
            data.addExp((float) exp);
            dropLoc.getWorld().dropItem(dropLoc, drop);
        }
        updateStage(e.getBlock(), 5, 0);
        updateStage(e.getBlock(), (long) (5 + Math.random() * 3), 1);
        updateStage(e.getBlock(), (long) (8 + Math.random() * 4), 1);
        updateStage(e.getBlock(), (long) (12 + Math.random() * 5), 2);
        updateStage(e.getBlock(), (long) (15 + Math.random() * 6), 3);
    }
    public void updateStage(@NotNull Block block, long time, int oldAge) {
        var blockLoc = block.getLocation();
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> {
            var b = blockLoc.getBlock();
            if (b.getType() == Material.AIR)
                return;
            Ageable age = (Ageable) b.getBlockData();
            if (age.getAge() == oldAge)
                return;
            age.setAge(age.getAge() + 1);
            b.setBlockData(age);
        }, 20L * time);
    }
}
