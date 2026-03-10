package me.r1ver.kinetic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KineticListener implements Listener {

	private double horizontalModifier;
	private double verticalModifier;

	private Method getHandleMethod;
	private Field pingField;
	private Object serverInstance;
	private Field tpsField;

	public KineticListener(double horizontal, double vertical) {
		this.horizontalModifier = horizontal;
		this.verticalModifier = vertical;
		setupReflection();
	}

	private void setupReflection() {
		try {
			getHandleMethod = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer").getMethod("getHandle");
			pingField = Class.forName("net.minecraft.server.v1_8_R3.EntityPlayer").getField("ping");
			Class<?> serverClass = Class.forName("net.minecraft.server.v1_8_R3.MinecraftServer");
			serverInstance = serverClass.getMethod("getServer").invoke(null);
			tpsField = serverClass.getField("recentTps");
		} catch (Exception e) {
			Bukkit.getLogger().warning("[Kinetic] Erro ao carregar reflexão NMS. Algumas funções de lag podem falhar.");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onVelocity(PlayerVelocityEvent event) {
		if (event.getPlayer().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event.getPlayer().getLastDamageCause();
			if (damageEvent.getDamager() instanceof Player) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;

		Player damaged = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();

		if (damaged.getNoDamageTicks() > damaged.getMaximumNoDamageTicks() / 2.0)
			return;

		double tpsModifier = 1.0;
		double pingModifier = 1.0;

		if (Kinetic.getInstance().getConfig().getBoolean("settings.compensate-lag", true)) {
			double tps = getRecentTPS();
			tpsModifier = (tps < 19.0) ? (20.0 / Math.max(tps, 1.0)) : 1.0;
		}

		if (Kinetic.getInstance().getConfig().getBoolean("settings.compensate-ping", true)) {
			int ping = getPing(damaged);
			pingModifier = (ping > 150) ? 1.07 : 1.0;
		}

		double mSprint = damaged.isSprinting() ? 0.8 : 0.5;
		int kbLevel = damager.getItemInHand() == null ? 0
				: damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
		double mKnockbackEnchant = kbLevel * 0.45;
		Location locDamaged = damaged.getLocation();
		Location locDamager = damager.getLocation();

		Vector knockback = locDamaged.toVector().subtract(locDamager.toVector());
		if (knockback.lengthSquared() > 0) {
			knockback.normalize();
		} else {
			knockback = new Vector(0, 0.4, 0);

			double xzMod = (mSprint + mKnockbackEnchant) * horizontalModifier * tpsModifier * pingModifier;
			@SuppressWarnings("deprecation")
			double yMod = 0.4 * (damaged.isOnGround() ? 1.0 : 0.7) * verticalModifier * tpsModifier;

			knockback.setX(knockback.getX() * xzMod);
			knockback.setY(yMod);
			knockback.setZ(knockback.getZ() * xzMod);
			Kinetic.getInstance().getVelocityVersion().sendVelocityPacket(damaged, knockback);

		}
	}

	private int getPing(Player player) {
		try {
			Object entityPlayer = getHandleMethod.invoke(player);
			return pingField.getInt(entityPlayer);
		} catch (Exception e) {
			return 0;
		}
	}

	private double getRecentTPS() {
		try {
			double[] tps = (double[]) tpsField.get(serverInstance);
			return tps[0];
		} catch (Exception e) {
			return 20.0;
		}
	}

	public void setHorizontalModifier(final double horizontalModifier) {
		this.horizontalModifier = horizontalModifier;
	}

	public void setVerticalModifier(final double verticalModifier) {
		this.verticalModifier = verticalModifier;
	}

	public double getHorizontalModifier() {
		return this.horizontalModifier;
	}

	public double getVerticalModifier() {
		return this.verticalModifier;
	}
}