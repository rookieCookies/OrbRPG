package orbrpg.events;

import orbrpg.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerAttackedEvent implements Listener {
    @EventHandler
    public void onDamageToPlayer(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        Player p = (Player) e.getEntity();
        PlayerData data = new PlayerData(p);
        float defense = data.getFloat("defense");
        float damage = (float) e.getDamage() - defense;
        if (damage < 1) damage = 1;
        float currentHealth = data.getFloat("current_health");
        currentHealth -= damage;
        data.setFloat("current_health", currentHealth);
        e.setDamage(0.0);
    }
}
