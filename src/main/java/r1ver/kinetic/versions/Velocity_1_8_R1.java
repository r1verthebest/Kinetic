package me.r1ver.kinetic.versions;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;

public class Velocity_1_8_R1 implements IVelocityVersion {

	@Override
	public void sendVelocityPacket(Player player, Vector vector) {
		PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(player.getEntityId(), vector.getX(),
				vector.getY(), vector.getZ());

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}