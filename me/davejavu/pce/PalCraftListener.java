package me.davejavu.pce;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import me.davejavu.davechat.DaveListener;
import me.davejavu.pce.command.afk;
import me.davejavu.pce.command.back;
import me.davejavu.pce.command.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class PalCraftListener implements Listener {

	static Plugin plugin;

	public PalCraftListener(PalCraftEssentials plugin) {
		PalCraftListener.plugin = plugin;
	}
	public static boolean ultramute = false;

	public static String[] commands;

	public static List<String> moo = new ArrayList<String>();
	public static List<String> gag = new ArrayList<String>();

	public static HashMap<String, Long> play = new HashMap<String, Long>();
	public static HashMap<String, Integer> stalk = new HashMap<String, Integer>();
	public static HashMap<String, Boolean> kick = new HashMap<String, Boolean>();

	public static List<String> online = new ArrayList<String>();

	Logger log = Logger.getLogger("Minecraft");

	public static String host = PalCommand.getConfig().getFC().getString("mysql.host");
	public static String port = PalCommand.getConfig().getFC().getString("mysql.port");
	public static String database = PalCommand.getConfig().getFC().getString("mysql.database");
	public static String username = PalCommand.getConfig().getFC().getString("mysql.username");
	public static String password = PalCommand.getConfig().getFC().getString("mysql.password");
	public static String date = PalCraftEssentials.date;

	public static HashMap<String, String> fOW = new HashMap<String, String>();
	public static HashMap<String, String> tempDeathStorage = new HashMap<String, String>();

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent evt) {
		if (PalCommand.getConfig().getFC().getStringList("blocked-animal-spawn-worlds").contains(evt.getEntity().getWorld().getName())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onPotion(PotionSplashEvent evt) {
		if (PalCommand.getConfig().getFC().getStringList("blocked-potion-throw-worlds").contains(evt.getEntity().getWorld().getName())) {
			log.info("[PCE-BLOCK] " + ((Player)evt.getPotion().getShooter()).getName() + " tried to throw a potion!");
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemHeldChange(PlayerItemHeldEvent evt) {
		if (PalCommand.getConfig().getFC().getStringList("blocked-animal-spawn-worlds").contains(evt.getPlayer().getWorld().getName())) {
			if (evt.getPlayer().getInventory().getItemInHand().getTypeId() == 373 || evt.getPlayer().getInventory().getItemInHand().getTypeId() == 383 || evt.getPlayer().getInventory().getItemInHand().getTypeId() == 384) {
				log.info("[PCE-BLOCK] " + evt.getPlayer().getName() + " tried to hold " + evt.getPlayer().getInventory().getItemInHand().getType().toString().toLowerCase());
				evt.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
			}
		}
	}

	@EventHandler
	public void onPistonPush(BlockPistonExtendEvent evt) {
		for (Block bl : evt.getBlocks()) {
			boolean iB = isBlockedToPush(bl);
			if (iB) {
				evt.setCancelled(true);
				break;
			}
		}

	}

	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent evt) {
		Block block = evt.getBlock().getWorld().getBlockAt(evt.getRetractLocation());
		boolean iB = isBlockedToPush(block);
		if (iB) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent evt) {
		Player player = evt.getPlayer();
		String l1 = evt.getLine(0);
		String l2 = evt.getLine(1);
		if (l1.equalsIgnoreCase("[warp]")) {
			evt.setLine(0, ChatColor.AQUA + "[Warp]");
			if (PalCraftEssentials.getWarp(l2) == null) {
				evt.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Warp '" + ChatColor.WHITE + l2 + ChatColor.RED + "' doesn't exist!");
				return;
			}
			if (!player.hasPermission("PalCraftEssentials.sign.warp.create")) {
				evt.setCancelled(true);
				player.sendMessage(ChatColor.RED + "No permission to place " + ChatColor.WHITE + "warp" + ChatColor.RED + " sign");
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent evt) {
		Player player = evt.getPlayer();
		if (!player.hasPermission("PalCraftEssentials.exempt.ban")) {
			String br = " ";
			try{
				br = isBanned(player);
			} catch (Exception e) {
				evt.setResult(Result.KICK_BANNED);
				evt.setKickMessage("A MySQL error occurred - try again later.");
				e.printStackTrace();
			}
			if (br != null) {
				if (!br.equalsIgnoreCase(" ")) {
					evt.setResult(Result.KICK_BANNED);
					evt.setKickMessage(br);
				}
			} else {
				evt.setResult(Result.KICK_BANNED);
				evt.setKickMessage("A MySQL error occurred - try again later.");
			}

		} else {
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You were banned! (You are exempt)");
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		CustomConfig conf = PalCommand.getConfig(player);
		if (conf.getFC().getBoolean("superpick") && (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE)) {
			if (evt.getBlock().getTypeId() != 7 || player.hasPermission("PalCraftEssentials.superpick.bedrock")) {
				evt.setInstaBreak(true);
			}
		}
	}

	@EventHandler
	public void onPlayerClick(PlayerInteractEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if ((evt.getAction() == Action.RIGHT_CLICK_BLOCK) && (evt.getClickedBlock().getType() == Material.CHEST) && (vanish.isVanished(player))) {
			evt.setCancelled(true);
			Chest chest = (Chest) evt.getClickedBlock().getState();
			Inventory i = plugin.getServer().createInventory(player, chest.getInventory().getSize());
			i.setContents(chest.getInventory().getContents());
			player.openInventory(i);
			player.sendMessage(ChatColor.GOLD + "Opening chest silently - no editing.");

			return;
		}
		try{
			if (((evt.getAction() == Action.RIGHT_CLICK_BLOCK) && (evt.getClickedBlock().getType() == Material.SIGN) || (evt.getClickedBlock().getType() == Material.SIGN_POST) || (evt.getClickedBlock().getType() == Material.WALL_SIGN))) {
				if (evt.getAction() != Action.LEFT_CLICK_BLOCK) {
					Sign s = (Sign)evt.getClickedBlock().getState();
					String l1 = s.getLine(0);
					String l2 = s.getLine(1);
					if (l1.equals(ChatColor.AQUA + "[Warp]")) {
						if (player.hasPermission("PalCraftEssentials.sign.warp.use")) {
							Location loc = PalCraftEssentials.getWarp(l2);
							if (loc == null) {
								player.sendMessage(ChatColor.RED + "Warp '" + ChatColor.WHITE + l2 + ChatColor.RED + "' doesn't exist!");
								return;
							}
							player.teleport(loc);
						} else {
							player.sendMessage(ChatColor.RED + "No permission for " + ChatColor.WHITE + "warp" + ChatColor.RED + " sign");
							return;
						}

					}
				}
			}
		}catch (Exception e){}
		if (stalk.containsKey(player.getName())) {
			int p = stalk.get(player.getName()) + 1;
			player.teleport(Bukkit.getServer().getPlayer(online.get(p)));
			player.sendMessage(ChatColor.GOLD + "Now stalking: " + ChatColor.WHITE + online.get(p));
			stalk.remove(player.getName());
			stalk.put(player.getName(), p);
		}
		if ((evt.getAction() == Action.LEFT_CLICK_AIR) || (evt.getAction() == Action.LEFT_CLICK_BLOCK)) {
			if (player.getItemInHand().getType() == Material.BLAZE_ROD && player.hasPermission("PalCraftEssentials.launchfireball")) {
				Location playerLoc = player.getLocation();
				playerLoc.setY(playerLoc.getY() + 2);
				player.getWorld().spawn(playerLoc, SmallFireball.class);
			}
		}

	}

	@EventHandler
	public void onShear(PlayerShearEntityEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if (vanish.isVanished(player) && !vanish.canInteract(player)) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if (vanish.isVanished(player) && !vanish.canInteract(player)) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if (vanish.isVanished(player) && !vanish.canInteract(player)) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent evt) {
		//Set back location to death location
		back.setBack(evt.getEntity(), evt.getEntity().getLocation());
		//To restore items after people fall out of world, as it's a bug.
		if (evt.getDeathMessage().equalsIgnoreCase(evt.getEntity().getName() + " fell out of the world")) {
			Player player = evt.getEntity();
			afk.updateActivity(player);
			if (fOW.containsKey(player.getName().toLowerCase())) {
				fOW.remove(player.getName().toLowerCase());
			}
			fOW.put(player.getName().toLowerCase(), tempDeathStorage.get(player.getName().toLowerCase()));
		}
		/*if (evt.getDeathMessage().contains("was slain by")) {
			OfflinePlayer p2o = Bukkit.getOfflinePlayer(evt.getDeathMessage().split("was slain by")[1]);
			if (p2o.isOnline()) {
				ItemStack killedWith = p2o.getPlayer().getItemInHand();
				evt.setDeathMessage(ChatColor.RED + evt.getEntity().getDisplayName() + ChatColor.DARK_RED + " was killed by " + ChatColor.RED + p2o.getPlayer().getDisplayName() + ChatColor.DARK_RED + " with a " + ChatColor.RED + killedWith.getType().toString().toLowerCase());
			} else {
				evt.setDeathMessage(ChatColor.RED + evt.getEntity().getDisplayName() + ChatColor.DARK_RED + " was killed by " + ChatColor.RED + p2o.getPlayer().getDisplayName());
			}
		}*/
	}



	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent evt) {
		if ((evt.getCause() == TeleportCause.PLUGIN || evt.getCause() == TeleportCause.COMMAND)) {
			back.setBack(evt.getPlayer(), evt.getFrom());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent evt){
		plugin.reloadConfig();
		Player player = evt.getPlayer();
		//Start of playtime - then either when a player/console types /playtime
		//the playtime in their config will be updated as to give the most
		//accurate playtime, down to seconds.
		play.put(player.getName().toLowerCase(), System.currentTimeMillis());


		if (!player.hasPermission("PalCraftEssentials.command.stalk")) {
			online.add(player.getName());
		}
		CustomConfig playerCustomConfig = new CustomConfig("./plugins/PalCraftEssentials/players", player.getName().toLowerCase());
		FileConfiguration fc = playerCustomConfig.getFC();
		if (!fc.contains("firstplayed")) {
			fc.set("firstplayed", date);
		}
		if (!fc.contains("mute.boolean")) {
			fc.set("mute.boolean", false);
		}
		if (!fc.contains("mute.time")) {
			fc.set("mute.time", 0L);
		}
		if (!fc.contains("playtime")) {
			fc.set("playtime", 0L);
		}
		if (!fc.contains("god")) {
			fc.set("god", false);
		}
		if (!fc.contains("pvp")) {
			fc.set("pvp", false);
		}
		if (!fc.contains("tp")) {
			fc.set("tp", false);
		}
		if (!fc.contains("socialspy")) {
			fc.set("socialspy", false);
		}
		if (!fc.contains("vanish.boolean")) {
			fc.set("vanish.boolean", false);
		}
		if (!fc.contains("vanish.interact")) {
			fc.set("vanish.interact", true);
		}
		if (!fc.contains("vanish.chat")) {
			fc.set("vanish.chat", true);
		}
		if (!fc.contains("block-list")) {
			fc.set("block-list", new ArrayList<String>());
		}
		if (!fc.contains("back.world")) {
			fc.set("back.world", player.getWorld().getName());
			fc.set("back.x", Double.parseDouble("3"));
			fc.set("back.y", Double.parseDouble("3"));
			fc.set("back.z", Double.parseDouble("3"));
			fc.set("back.yaw", Float.parseFloat("3"));
			fc.set("back.pitch", Float.parseFloat("3"));
		}
		if (!fc.contains("ip")) {
			fc.set("ip", player.getAddress().getHostString());
		}
		if (!fc.contains("superpick")) {
			fc.set("superpick", false);
		}
		if (!fc.contains("block-commands.boolean")) {
			fc.set("block-commands.boolean", false);
			fc.set("block-commands.time", 0L);
		}
		if (!fc.contains("afk")) {
			fc.set("afk", false);
		}
		if (!fc.contains("censor")) {
			fc.set("censor", false);
		}
		playerCustomConfig.save();


		play.put(player.getName().toLowerCase(), System.currentTimeMillis());
		if (!plugin.getConfig().contains("playtime." + player.getName().toLowerCase())) {
			plugin.getConfig().set("playtime." + player.getName().toLowerCase(), Long.parseLong("0"));
			plugin.saveConfig();
		}
		String ip = player.getAddress().getHostString();
		CustomConfig ipConfig = new CustomConfig("./plugins/PalCraftEssentials", "ip.yml");
		if (ipConfig.getFC().contains("ip." + ip)){
			String pip = ipConfig.getFC().getString("ip." + ip);
			if (!pip.contains(player.getName())){
				String newpip = pip + player.getName() + ";";
				ipConfig.getFC().set("ip." + ip, newpip);
				ipConfig.save();
			}
			String[] ps = pip.split(";");
			StringBuilder l = new StringBuilder();
			for (String x : ps){
				if (!x.equals(player.getName())){
					String iB = " ";
					iB = isBanned(Bukkit.getOfflinePlayer(x));
					if (!iB.equals(" ")){
						l.append(ChatColor.DARK_RED + "" + ChatColor.BOLD + "(BANNED) " + ChatColor.RESET + ChatColor.RED + x + ChatColor.WHITE + ",");
					} else {
						l.append(ChatColor.RED + x + ChatColor.WHITE + ",");
					}
				}
			}
			if (!l.toString().equals("")){
				for (Player p : Bukkit.getServer().getOnlinePlayers()){
					if (p.hasPermission("PalCraftEssentials.alt.view")){
						p.sendMessage(ChatColor.GOLD + player.getName() + " may have alt accounts: " + l.toString());
					}
				}
				PalCraftEssentials.log.info(player.getName() + " may have alt accounts: " + l.toString());
			}
		}else{
			ipConfig.getFC().set("ip." + ip, player.getName() + ";");
			ipConfig.save();
		}

		DateFormat dF = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		player.sendMessage(ChatColor.GOLD + "The date is: " + ChatColor.WHITE + dF.format(date));
		String[] d = dF.format(date).split("/");
		if (Integer.parseInt(d[0]) == 1 && Integer.parseInt(d[1]) == 8) {
			player.sendMessage(ChatColor.GOLD + "Don't forget - it's Daves birthday!! :D");
		}
		if (dF.format(date).startsWith("25/12/")) {
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MERRY CHRISTMAS!");
		}
		CustomConfig pc = PalCommand.getConfig(player);
		if (pc.getFC().getBoolean("vanish")) {
			vanish.setVanish(player, true);
			evt.setJoinMessage(null);
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Joined silently");
		} else {
			if (player.hasPermission("PalCraftEssentials.command.vanish")) {
				pc.getFC().set("vanish.boolean", true);
				pc.getFC().set("vanish.chat", false);
				pc.getFC().set("vanish.interact", false);
				pc.save();
				evt.setJoinMessage(null);


				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission("PalCraftEssentials.command.vanish")) {
						p.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " joined silently");
					}
				}
				vanish.setVanish(player, true);
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Joined silently");
			} else {
				evt.setJoinMessage(join(evt.getPlayer()));
			}

		}
		List<String> vanList = vanish.listOnlineVanished();
		if (!player.hasPermission("PalCraftEssentials.command.vanish")) {
			for (String s : vanList) {
				Player p = Bukkit.getPlayer(s);
				player.hidePlayer(p);
			}
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (vanList.contains(Bukkit.getOfflinePlayer(p.getName()))) {
				player.hidePlayer(p);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent evt) {
		Entity e = evt.getEntity();
		if (e instanceof Player) {
			Player player = (Player) e;
			afk.updateActivity(player);
			boolean god = PalCommand.getConfig(player).getFC().getBoolean("god");
			boolean va = vanish.isVanished(player);
			if (god || va) {
				evt.setCancelled(true);
			}
			if (tempDeathStorage.containsKey(player.getName().toLowerCase()))
				tempDeathStorage.remove(player.getName().toLowerCase());
			String s = invToString(player.getInventory(), player.getName());
			if (!s.equalsIgnoreCase(";"))
				tempDeathStorage.put(player.getName().toLowerCase(), invToString(player.getInventory(), player.getName()));
		}

	}

	@EventHandler
	public void onEntityDamageByDamageEvent(EntityDamageByEntityEvent evt) {
		Entity e = evt.getEntity();
		if (e instanceof Player) {
			Player player = (Player) e;
			afk.updateActivity(player);
			boolean god = PalCommand.getConfig(player).getFC().getBoolean("god");
			boolean pvp = PalCommand.getConfig(player).getFC().getBoolean("pvp");
			//Doing damage
			if (evt.getDamager() instanceof Player) {
				Player damager = (Player) evt.getDamager();
				boolean godP = PalCommand.getConfig(damager).getFC().getBoolean("god");
				boolean pvpP = PalCommand.getConfig(damager).getFC().getBoolean("pvp");
				if (!((pvpP && !godP) && (pvp && !god))) {
					evt.setCancelled(true);
				}
			}
			if (vanish.isVanished(player) && !vanish.canInteract(player)) {
				evt.setCancelled(true);
			}
		}
		if (e.getType() == EntityType.WITHER) {
			e.remove();
		}
		if (evt.getDamager().getType() == EntityType.WITHER) {
			evt.getDamager().remove();
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent evt) {
		if (evt.getTarget() instanceof Player) {
			Player player = (Player) evt.getTarget();
			if (vanish.isVanished(player) && !vanish.canInteract(player)) {
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		CustomConfig pC = PalCommand.getConfig(player);
		FileConfiguration fc = pC.getFC();
		if (fc.getBoolean("freeze.boolean")) {
			String a1 = fc.getString("freeze.time");
			if (a1.equalsIgnoreCase("forever")) {
				player.sendMessage(ChatColor.RED + "Frozen!");
				Location back = new Location(evt.getFrom().getWorld(), evt.getFrom().getX(), evt.getFrom().getY(), evt.getFrom().getZ());
				player.teleport(back);
			} else {
				long diff = fc.getLong("freeze.time") - System.currentTimeMillis();
				long a2 = Long.parseLong(a1);
				if(diff <= 0 && a2 != 0){
					fc.set("freeze.boolean", false);
					pC.save();
				} else {
					player.sendMessage(ChatColor.RED + "Frozen for "+diff/1000/60+" more minutes!");
					Location back = new Location(evt.getFrom().getWorld(), evt.getFrom().getX(), evt.getFrom().getY(), evt.getFrom().getZ());
					player.teleport(back);
				}
			}

		}
		if (player.isFlying()) {
			int x = (int) player.getLocation().getX();
			int y = (int) player.getLocation().getY() - 1;
			int z = (int) player.getLocation().getZ();
			Location bLoc = player.getLocation();
			if ((player.getLocation().getWorld().getBlockAt(x,y,z).getType() == Material.SPONGE) && (bLoc.getWorld().getBlockAt(x+1, y, z).getType() == Material.BEDROCK) && (bLoc.getWorld().getBlockAt(x-2, y, z).getType() == Material.BEDROCK) && (bLoc.getWorld().getBlockAt(x, y, z+1).getType() == Material.BEDROCK) && (bLoc.getWorld().getBlockAt(x, y, z-2).getType() == Material.BEDROCK)) {
				player.setVelocity(new Vector(0.0, 20.0, 0.0));
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt) {
		Player player = evt.getPlayer();
		//long l = play.get(player.getName().toLowerCase());
		//play.remove(player.getName().toLowerCase());
		//plugin.getConfig().set("playtime." + player.getName().toLowerCase(), plugin.getConfig().getLong("platime." + player.getName().toLowerCase()) + (System.currentTimeMillis() - l));
		//plugin.saveConfig();

		updatePlaytime(player, false);
		online.remove(player.getName());
		if (kick.containsKey(player.getName().toLowerCase())) {
			kick.remove(player.getName().toLowerCase());
			evt.setQuitMessage(null);
		}
		if (vanish.isVanished(player)) {
			evt.setQuitMessage(null);
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("PalCraftEssentials.command.vanish")) {
					p.sendMessage(ChatColor.DARK_AQUA + player.getName() + " " + ChatColor.AQUA + " quit silently");
				}
			}
		} else {
			evt.setQuitMessage(quit(player));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if (vanish.isVanished(player) && !vanish.canInteract(player)) {
			evt.setCancelled(true);
		}
		Block block = evt.getBlock();
		if (((block.getType() == Material.SIGN) || (block.getType() == Material.SIGN_POST) || (block.getType() == Material.WALL_SIGN))) {
			Sign s = (Sign)block.getState();
			if (s.getLine(0).equals(ChatColor.AQUA + "[Warp]")) {
				if (!player.hasPermission("PalCraftEssentials.sign.warp.remove")) {
					evt.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You can't remove " + ChatColor.WHITE + "warp" + ChatColor.RED + " signs!");
				}
			}
		}
		Location loc2 = block.getLocation();
		loc2.setY(loc2.getY() + 1);
		Block b2 = loc2.getWorld().getBlockAt(loc2);
		if (b2.getType() == Material.SIGN_POST) {
			Sign s = (Sign) b2.getState();
			if (s.getLine(0).equals(ChatColor.AQUA + "[Warp]")) {
				if (!player.hasPermission("PalCraftEssentials.sign.warp.remove")) {
					evt.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You can't remove " + ChatColor.WHITE + "warp" + ChatColor.RED + " signs!");
				}
			}
		}
		CustomConfig conf = PalCommand.getConfig(player);
		if (conf.getFC().getBoolean("superpick") && (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE)) {
			ItemStack i = player.getItemInHand();
			i.setDurability((short) (i.getDurability() - 1));
			player.setItemInHand(i);
		}

	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt) {
		Player player = evt.getPlayer();
		afk.updateActivity(player);
		if (vanish.isVanished(player) && !vanish.canInteract(player)) {
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onPCPE(PlayerCommandPreprocessEvent evt) {
		if (evt.getMessage().equalsIgnoreCase("superpick") || evt.getMessage().equalsIgnoreCase("//") || evt.getMessage().equals("/")) {
			evt.setCancelled(true);
			Bukkit.getServer().dispatchCommand((CommandSender) evt.getPlayer(), "psp");
		}
		Player player = evt.getPlayer();
		CustomConfig conf = PalCommand.getConfig(player);
		FileConfiguration fc = conf.getFC();
		if (fc.getBoolean("block-commands.boolean")) {
			String f = fc.getString("block-commands.time");
			if (f.equalsIgnoreCase("forever")) {
				player.sendMessage(ChatColor.RED + "Cmds blocked!");
				evt.setCancelled(true);
			} else {
				long diff = Long.parseLong(f) - System.currentTimeMillis();
				if(diff <= 0){
					fc.set("block-commands.boolean", false);
					conf.save();
				} else {
					player.sendMessage(ChatColor.RED + "Cmds blocked for "+diff/1000/60+" more minutes!");
					evt.setCancelled(true);
				}
			}

		}

	}



	public static String isBanned(OfflinePlayer player) {
		String banreason = " ";
		//Permanent ban
		boolean banned = false;	
		ResultSet rpl = MySQL.getRow(MySQL.con, "perma_bans", "player='" + player.getName() + "'");
		
		try{
			while(rpl.next()) {
				if (player.getName().equalsIgnoreCase(rpl.getString("player"))) {
					banreason = ChatColor.GOLD + "You were banned by " + ChatColor.RED + rpl.getString("staff") + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + rpl.getString("reason") + ChatColor.GOLD + "\nAppeal at palcraft.com";
					banned = true;
					break;
				}
			}
		} catch (Exception e) {
		}
		if (banned) {
			return banreason;
		}
		long l = 1;
		int id = -1;
		boolean b = false;
		String when = "";
		String staff = "";
		String length = "";
		String reason = "";
		
		ResultSet rtm = MySQL.getRow(MySQL.con, "temp_bans", "player='" + player.getName() + "'");
		try{
			while(rtm.next()) {
				
				if (player.getName().equalsIgnoreCase(rtm.getString("player"))) {
					staff = rtm.getString("staff");
					length = rtm.getString("length");
					reason = rtm.getString("reason");
					String[] s = rtm.getString("length").split(" ");
					l = parseTimeSpec(s[0],s[1]);
					id = rtm.getInt("id");
					b = true;
					when = rtm.getString("when");
					break;
				}
			}
		} catch (Exception e) {}
		if (b){
			long wh = Long.parseLong(when);
			long tempTime = l;
			long now = System.currentTimeMillis();
			long diff = (tempTime + wh) - now;


			if(diff <= 0){
				MySQL.deleteRow(MySQL.con, "temp_bans", id, "player = '" + player.getName().toLowerCase() + "'");
				banreason = " ";
			}else{
				banreason = ChatColor.GOLD + "You were temp banned by " + ChatColor.RED + staff + ChatColor.GOLD+"\nLength" + ChatColor.WHITE + ": " + ChatColor.RED + length + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + reason + ChatColor.GOLD + "\nAppeal at palcraft.com\nWhen to come back" + ChatColor.WHITE + ": " + ChatColor.RED + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date(parseTimeSpec(length.split(" ")[0], length.split(" ")[1]) + Long.parseLong(when))));
			}
		}
		return banreason;
	}
	public static long parseTimeSpec(String time, String unit) {
		long ms = Integer.parseInt(time) * 1000;
		if (unit.startsWith("sec")) {
			return ms;
		} else if (unit.startsWith("min")) {
			ms = ms * 60;
		} else if (unit.startsWith("hour")) {
			ms = (ms * 60) * 60;
		} else if (unit.startsWith("day")) {
			ms = ms * (24 * 60 * 60);
		} else if (unit.startsWith("month")) {
			ms = ms * (24 * 60 * 60 * 30);
		} else if (unit.startsWith("mins")) {
			ms = ms * 60;
		} else if (unit.startsWith("hours")) {
			ms = (ms * 60) * 60;
		} else if (unit.startsWith("days")) {
			ms = ms * (24 * 60 * 60);
		} else if (unit.startsWith("months")) {
			ms = ms * (24 * 60 * 60 * 30);
		}

		return ms;

		// 1000ms = 1 s
		// 60000ms = 1 m
		// 3600000ms = 1 hr
		// 86400000ms = 1 day
	}



	public static String invToString(Inventory inv, String pname) {
		String rs = ";";
		for (ItemStack i : inv.getContents()) {
			if (i != null) {
				rs = rs + i.getTypeId() + "," + i.getAmount() + "," + i.getData().getData() + ";";
			}
		}
		return rs;
	}
	public static void stringSetInv(String str, Player owner) {
		for (String sa : str.split(";")) {
			String[] p = sa.split(",");
			ItemStack tA = new ItemStack(Integer.parseInt(p[0]), Integer.parseInt(p[1]), (byte) Integer.parseInt(p[2]));
			Bukkit.broadcastMessage("added " + p[1] +" " + p[0]);
			owner.getInventory().addItem(tA);
		}
	}
	public static String getRandomPlayer() {
		return Bukkit.getOnlinePlayers()[new Random().nextInt(Bukkit.getOnlinePlayers().length - 1)].getDisplayName();
	}
	public static void updatePlaytime(Player player, boolean b) {
		CustomConfig conf = PalCommand.getConfig(player.getName().toLowerCase());
		if (play.containsKey(player.getName().toLowerCase())) {
			long l = play.get(player.getName().toLowerCase());
			long pl = System.currentTimeMillis() - l;
			long oldPlay = conf.getFC().getLong("playtime");
			long newPlay = oldPlay + pl;
			conf.getFC().set("playtime", newPlay);
			conf.save();
			play.remove(player.getName().toLowerCase());
		}

		if (b) {
			play.put(player.getName().toLowerCase(), System.currentTimeMillis());
		}

	}

	public boolean isBlockedToPush(Block block) {
		List<Integer> il = PalCommand.getConfig().getFC().getIntegerList("blocked-piston-push");
		boolean b = false;
		for (int i : il) {
			if (block.getTypeId() == i) {
				b = true;
			}
		}
		return b;
	}

	public static String join(Player player) {
		if (Bukkit.getPluginManager().isPluginEnabled("DaveChat")) {
			try{
				String jM = DaveListener.getJoinMessageForPlayer(player);
				if (jM.equalsIgnoreCase("")) {
					jM = ChatColor.YELLOW + player.getName() + " joined the game";
				}
				return jM;
			} catch (Exception e) {
				e.printStackTrace();
				return ChatColor.YELLOW + player.getName() + " joined the game";
			}
		}
		return ChatColor.YELLOW + player.getName() + " joined the game";


	}
	public static String quit(Player player) {
		if (Bukkit.getPluginManager().isPluginEnabled("DaveChat")) {
			try{
				String jM = DaveListener.getQuitMessageForPlayer(player);
				if (jM.equalsIgnoreCase("")) {
					jM = ChatColor.YELLOW + player.getName() + " left the game";
				}
				return jM;
			} catch (Exception e) { 
				e.printStackTrace();
				return ChatColor.YELLOW + player.getName() + " left the game";
			}
		}
		return ChatColor.YELLOW + player.getName() + " left the game";
	}
}
