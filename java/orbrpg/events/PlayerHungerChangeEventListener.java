package orbrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerHungerChangeEventListener implements Listener {
    @EventHandler
    public void Event(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        PotionEffect potion = new PotionEffect(PotionEffectType.HUNGER, 200000, 0, false, false, false);
        e.getEntity().addPotionEffect(potion);
    }
}
