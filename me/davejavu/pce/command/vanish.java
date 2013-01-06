package me.davejavu.pce.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class vanish extends PalCommand {
	public vanish(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vanish")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.vanish")) {
					Player player = (Player) sender;
					if (args.length > 0) {
						if (args[0].equalsIgnoreCase("nc") || args[0].equalsIgnoreCase("nochat")) {
							CustomConfig pC = getConfig(player);
							boolean canChat = pC.getFC().getBoolean("vanish.chat");
							pC.getFC().set("vanish.chat", !canChat);
							pC.save();
							sendMessage(sender, ChatColor.GOLD + "Chat" + ChatColor.WHITE + ": " + (!canChat ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
							return true;
						} else if (args[0].equalsIgnoreCase("ni") || args[0].equalsIgnoreCase("nointeract")) {
							CustomConfig pCc = getConfig(player);
							boolean canInteract = pCc.getFC().getBoolean("vanish.interact");
							pCc.getFC().set("vanish.interact", !canInteract);
							pCc.save();
							sendMessage(sender, ChatColor.GOLD + "Interact" + ChatColor.WHITE + ": " + (!canInteract ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
							return true;
						} else if (args[0].equalsIgnoreCase("check")) {
							sendMessage(sender, ChatColor.GOLD + "Vanish: " + ChatColor.WHITE + ": " + (isVanished(player) ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
							return true;
						} else if (args[0].equalsIgnoreCase("fj") || args[0].equalsIgnoreCase("fakejoin")) {
							Bukkit.broadcastMessage(PalCraftListener.join(player));
							setVanish(player, false);
							warnPWP(player, false);
							
							return true;
						} else if (args[0].equalsIgnoreCase("fq") || args[0].equalsIgnoreCase("fakequit")) {
							Bukkit.broadcastMessage(PalCraftListener.quit(player));
							setVanish(player, true);
							warnPWP(player, true);
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /v <nc/ni/fj/fq/check>");
							return true;
						}
					} else {
						boolean b = toggleVanish(player);
						sendMessage(sender, ChatColor.GOLD + "Vanish" + ChatColor.WHITE + ": " + (b ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
						return true;
					}
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
				return true;
			}
		}
		return false;
	}
	
	public static void warnPWP(Player player, boolean isVanished) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("PalCraftEssentials.command.vanish")) {
				p.sendMessage(ChatColor.DARK_AQUA + player.getName() + " " + ChatColor.AQUA + "is now " + (isVanished ? "hidden" : "visible"));
			}
		}
	}
	
	public static boolean canChat(Player player) {
		return getConfig(player).getFC().getBoolean("vanish.chat");
	}
	
	public static boolean canInteract(Player player) {
		return getConfig(player).getFC().getBoolean("vanish.interact");
	}
	
	public static boolean isVanished(Player player) {
		return getConfig(player).getFC().getBoolean("vanish.boolean");
	}
	
	public static boolean toggleVanish(Player player) {
		CustomConfig config = getConfig(player);
		boolean isVanish = config.getFC().getBoolean("vanish.boolean");
		setVanish(player, !isVanish);
		return !isVanish;
	}
	public static void setVanish(Player player, boolean tf) {
		CustomConfig config = getConfig(player);
		config.getFC().set("vanish.boolean", tf);
		if (tf) {
			config.getFC().set("vanish.interact", false);
			config.getFC().set("vanish.chat", false);
		} else {
			config.getFC().set("vanish.interact", true);
			config.getFC().set("vanish.chat", true);
		}
		config.save();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("PalCraftEssentials.command.vanish.see")) {
				if (tf) {
					p.hidePlayer(player);
				} else {
					p.showPlayer(player);
				}
			}
		}
	}
	public static List<String> listOnlineVanished() {
		List<String> v = new ArrayList<String>();
		for (OfflinePlayer p : listVanished()) {
			if (p.isOnline()) {
				v.add(p.getName());
			}
		}
		return v;
	}
	public static List<OfflinePlayer> listVanished() {
		List<OfflinePlayer> vanishList = new ArrayList<OfflinePlayer>();
		List<String> ret = new ArrayList<String>();
		File file = new File("./plugins/PalCraftEssentials/players");
		for (String x : file.list()) {
			ret.add(x.replace(".yml", ""));
		}
		for (String r : ret) {
			CustomConfig config = new CustomConfig("./plugins/PalCraftEssentials/players", r);
			boolean van = config.getFC().getBoolean("vanish");
			if (van) {
				vanishList.add(Bukkit.getOfflinePlayer(r.replace(".yml", "")));
			}
		}
		return vanishList;
	}
}
