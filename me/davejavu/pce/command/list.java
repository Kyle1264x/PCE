package me.davejavu.pce.command;

import java.util.logging.Level;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class list extends PalCommand {
	
	public list(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("list")) {
			boolean gmE = Bukkit.getPluginManager().isPluginEnabled("GroupManager");
			GroupManager gm = null;
			AnjoPermissionsHandler handler;
			if (gmE) {
				gm = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
			}
			StringBuilder sb = new StringBuilder();
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (!vanish.isVanished(p)) {
					String fName = "";
					if (gmE) {
						if (gm != null) {
							handler = gm.getWorldsHolder().getWorldPermissions(p);
							if (handler != null) {
								String name = p.getDisplayName();
								String gmPrefix = handler.getUserPrefix(p.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
								String gmSuffix = handler.getUserSuffix(p.getName()).replaceAll("&([0-9a-rA-R])", "§$1");
								String fm = "%s%s%s";
								fName = String.format(fm, gmPrefix, name, gmSuffix);
							}
						} else {
							PalCraftEssentials.log.log(Level.SEVERE, "GM == null!");
						}
					} else {
						fName = p.getDisplayName();
					}
					sb.append(fName + ChatColor.WHITE + ", ");
				}
			}
			sendMessage(sender, ChatColor.GOLD + "Players online " + ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().length  + "/" + Bukkit.getMaxPlayers() + "):");
			sendMessage(sender, ChatColor.WHITE + sb.toString());
			return true;
		}
		return false;
	}

}
