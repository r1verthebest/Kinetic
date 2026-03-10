package me.r1ver.kinetic.versions;

import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Velocity_1_7_10 implements IVelocityVersion {

    @Override
    public void sendVelocityPacket(Player player, Vector vector) {
        PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(
                player.getEntityId(), 
                vector.getX(), 
				vector.getY(), 
                vector.getZ()
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}