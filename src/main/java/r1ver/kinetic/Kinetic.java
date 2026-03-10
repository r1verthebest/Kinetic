package me.r1ver.kinetic;

import java.util.logging.Level;

import me.r1ver.kinetic.commands.KineticCommand;
import me.r1ver.kinetic.versions.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Kinetic extends JavaPlugin {

	private static Kinetic instance;
	private IVelocityVersion velocityVersion;
	private KineticListener listener;

	@Override
	public void onEnable() {
		instance = this;

		if (!setupVersionControl()) {
			getLogger().log(Level.SEVERE, "Versão NMS não suportada! O plugin será desativado.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		saveDefaultConfig();
		initPlugin();

		getLogger().info("Kinetic v" + getDescription().getVersion() + " ativado com sucesso!");
	}

	private void initPlugin() {
		double horizontal = getConfig().getDouble("horizontal", 0.0D);
		double vertical = getConfig().getDouble("vertical", 0.0D);

		this.listener = new KineticListener(horizontal, vertical);
		Bukkit.getPluginManager().registerEvents(this.listener, this);
		getCommand("kinetic").setExecutor(new KineticCommand(instance));
	}

	private boolean setupVersionControl() {
		String version;
		try {
			version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		} catch (Exception e) {
			return false;
		}

		getLogger().info("Versão detectada: " + version);
		switch (version.toLowerCase()) {
		case "v1_7_r4":
			this.velocityVersion = new Velocity_1_7_10();
			break;
		case "v1_8_r1":
			this.velocityVersion = new Velocity_1_8_R1();
			break;
		case "v1_8_r2":
			this.velocityVersion = new Velocity_1_8_R2();
			break;
		case "v1_8_r3":
			this.velocityVersion = new Velocity_1_8_R3();
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public void onDisable() {
		getLogger().info("Kinetic desativado.");
	}

	public static Kinetic getInstance() {
		return instance;
	}

	public IVelocityVersion getVelocityVersion() {
		return this.velocityVersion;
	}

	public KineticListener getListener() {
		return this.listener;
	}
}