package me.davejavu.pce;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PalCommand implements CommandExecutor {
	public abstract boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args);
	public static void sendMessage(CommandSender sender, String message) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(message);
		} else {
			PalCraftEssentials.log.info(message);
		}
	}
	public static boolean permissionCheck(CommandSender sender, String permission) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			return p.hasPermission(permission);
		} else {
			return true;
		}
	}
	public static void noPermission(String commandName, CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "No permission: '" + ChatColor.WHITE + commandName + ChatColor.RED + "'");
	}
	public Player getPlayer(String name) {
		Player p2 = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getDisplayName().contains(name)) {
				p2 = p;
			}
		}
		if (p2 == null) {
			List<Player> p2l = Bukkit.matchPlayer(name);
			if (p2l != null) {
				if (p2l.size() > 0) {
					p2 = p2l.get(0);
				}
			}
		}
		return p2;
	}
	public List<Player> getPlayerList(String name) {
		return Bukkit.matchPlayer(name);
	}
	
	public static String host = getConfig().getFC().getString("mysql.host");
	public static String port = getConfig().getFC().getString("mysql.port");
	public static String database = getConfig().getFC().getString("mysql.database");
	public static String username = getConfig().getFC().getString("mysql.username");
	public static String password = getConfig().getFC().getString("mysql.password");
	public static Logger log = Logger.getLogger("Minecraft");
	public static String date = PalCraftEssentials.date;
	public static CustomConfig getConfig() {
		return new CustomConfig("./plugins/PalCraftEssentials", "config.yml");
	}
	public static CustomConfig reloadConfig() {
		return new CustomConfig("./plugins/PalCraftEssentials", "config.yml");
	}
	public static void saveConfig(CustomConfig config) {
		config.save();
	}
	public static CustomConfig getConfig(Player player) {
		return getConfig(player.getName());
	}
	public static CustomConfig getConfig(String player) {
		return new CustomConfig("./plugins/PalCraftEssentials/players", player.toLowerCase() + ".yml");
	}
	public static CustomConfig getConfig(OfflinePlayer player) {
		return new CustomConfig("./plugins/PalCraftEssentials/players", player.getName().toLowerCase() + ".yml");
	}
}
