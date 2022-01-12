package orbrpg;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class ProtocolLib {
    private final Plugin plugin;

    public ProtocolLib(final Plugin plugin) {
        final long start = System.currentTimeMillis();
        this.plugin = plugin;
        if (!OrbRPG.getInstance().getConfig().getBoolean("features.disable_damage_particles"))
            return;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                if (packet.getNewParticles().read(0).getParticle() != Particle.DAMAGE_INDICATOR) {
                    return;
                }
                event.setCancelled(true);
            }
        });
        final long fin = System.currentTimeMillis() - start;
        String logMessage = "ProtocolLib has been registered successfully! (" + fin + "ms)";
        OrbRPG.getInstance().getLogger().log(Level.INFO, logMessage);
    }
}
