package orbrpg;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;

public class ProtocolLib {
    private final Plugin plugin;

    public ProtocolLib(final Plugin plugin) {
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
    }
}
