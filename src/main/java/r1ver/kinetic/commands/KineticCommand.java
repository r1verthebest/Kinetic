package me.r1ver.kinetic.commands;

import me.r1ver.kinetic.Kinetic;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KineticCommand implements CommandExecutor {

	private final Kinetic plugin;

	public KineticCommand(Kinetic plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("kinetic.admin")) {
			sendConfigMessage(sender, "messages.no-permission", "&cSem permissão.");
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			handleReload(sender);
			return true;
		}

		if (args.length == 2) {
			handleUpdate(sender, args[0], args[1]);
			return true;
		}

		sendHelp(sender);
		return true;
	}

	private void handleReload(CommandSender sender) {
		plugin.reloadConfig();

		double h = plugin.getConfig().getDouble("knockback.horizontal", 0.5);
		double v = plugin.getConfig().getDouble("knockback.vertical", 0.5);

		plugin.getListener().setHorizontalModifier(h);
		plugin.getListener().setVerticalModifier(v);

		sendConfigMessage(sender, "messages.reload-success", "&aConfiguração recarregada com sucesso!");
	}

	private void handleUpdate(CommandSender sender, String hRaw, String vRaw) {
		try {
			double h = Double.parseDouble(hRaw);
			double v = Double.parseDouble(vRaw);

			if (h < 0.0 || v < 0.0) {
				sendConfigMessage(sender, "messages.invalid-value", "&cUse apenas números positivos!");
				return;
			}

			plugin.getListener().setHorizontalModifier(h);
			plugin.getListener().setVerticalModifier(v);

			plugin.getConfig().set("knockback.horizontal", h);
			plugin.getConfig().set("knockback.vertical", v);
			plugin.saveConfig();

			sendConfigMessage(sender, "messages.update-success", "&aKnockback atualizado para H: " + h + " V: " + v);

		} catch (NumberFormatException e) {
			sendConfigMessage(sender, "messages.invalid-number", "&cValores numéricos inválidos.");
		}
	}

	private void sendHelp(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§3§lKinetic §7- §fSistema de Knockback");
		sender.sendMessage(" §b/kinetic <horizontal> <vertical> §7- Ajusta os valores.");
		sender.sendMessage(" §b/kinetic reload §7- Recarrega o plugin.");
		sender.sendMessage("");
	}

	private void sendConfigMessage(CommandSender sender, String path, String def) {
		String msg = plugin.getConfig().getString(path, def);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}