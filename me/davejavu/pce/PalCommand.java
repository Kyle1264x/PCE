package me.davejavu.pce;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PalCommand implements CommandExecutor {
	public abstract boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args);
	
	
	
	//Stuff that command classes can use, means I don't have to change them all if I get it wrong, I just have to change these methods.
	
	//Sends the commandsender a message, so if the CommandSender is a player, it will send them a message in game,
	//whereas if the CommandSender is the console, it will log the message.
	public static void sendMessage(CommandSender sender, String message) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(message);
		} else {
			PalCraftEssentials.log.info(message);
		}
	}
	
	//Simply returns a boolean if the sender has permission to execute a command,
	//simpler than making all classes check if it's either the console or the player has permission
	public static boolean permissionCheck(CommandSender sender, String permission) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			return p.hasPermission(permission);
		} else {
			return true;
		}
	}
	
	//Sends the sender a no permission message.
	public static void noPermission(String commandName, CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "No permission: '" + ChatColor.WHITE + commandName + ChatColor.RED + "'");
	}
	
	//So, this method is my favourite.
	//Firstly it goes through all players on the server, checking if their nickname contains what was
	//entered.  If it happens to find a player it will set the Player "p2" to the player found
	//The reason I did this was because I was fed up with having to /whois people to find their
	//real name to execute a command with them, so I enabled us to be able to type all or part of
	//their nickname for ease of use.
	//In the case that a player's nickname does not match the string inputted,
	//p2 will still be null, therefore the if statement will return true, therefore
	//it will check if there it can match a player with that name through Bukkit's method.
	//Then, it will return p2, whether it's null or not.
	//All I have to do in the class is check if the player returned is null, if it is then they're not online.
	public Player getPlayer(String name) {
		Player p2 = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getDisplayName().toLowerCase().contains(name.toLowerCase())) {
				p2 = p;
				break;
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
	
	//Returns the list given when Bukkit.matchPlayer() is used.  Pretty pointless :P
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
	
	//Config stuff, to make my life easier.
	
	//Returns the config made by the Bukkit methods
	public static CustomConfig getConfig() {
		return new CustomConfig("./plugins/PalCraftEssentials", "config.yml");
	}
	//Same as above, pointless.
	public static CustomConfig reloadConfig() {
		return new CustomConfig("./plugins/PalCraftEssentials", "config.yml");
	}
	//Saves the specified CustomConfig
	public static void saveConfig(CustomConfig config) {
		config.save();
	}
	//Returns the config for the player by using the method below.
	public static CustomConfig getConfig(Player player) {
		return getConfig(player.getName());
	}
	//Returns a players' config, in the players directory.
	public static CustomConfig getConfig(String player) {
		return new CustomConfig("./plugins/PalCraftEssentials/players", player.toLowerCase() + ".yml");
	}
	//Same as above but for an offline player
	public static CustomConfig getConfig(OfflinePlayer player) {
		return getConfig(player.getName());
	}
	
	
	public static String getGMName(Player player) {
		String name = "";
		if (Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
			GroupManager gm = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
			if (gm != null) {
				AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
				if (handler == null) {
					PalCraftEssentials.log.log(Level.SEVERE, "Handler == null!");
				} else {
					String gmPrefix = handler.getUserPrefix(player.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
					String gmSuffix = handler.getUserSuffix(player.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
					String fm = "%s%s%s";
					name = String.format(fm, gmPrefix, name, gmSuffix);
				}
			}
		} else {
			name = player.getDisplayName();
		}
		return name;
	}
}
