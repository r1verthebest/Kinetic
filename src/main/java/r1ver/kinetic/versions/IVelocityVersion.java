package me.r1ver.kinetic.versions;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface IVelocityVersion {

    void sendVelocityPacket(Player player, Vector velocity);
    
}