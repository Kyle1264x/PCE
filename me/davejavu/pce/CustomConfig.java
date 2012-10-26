package me.davejavu.pce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {
	FileConfiguration fileConfig = null;
	File fileConfigFile = null;
	String path;
	String fileName;
	public CustomConfig(String path, String name) {
		if (path.endsWith("/")) {
    		path.charAt(path.toCharArray().length - 1);
    		String[] pt = path.split("/");
    		StringBuilder np = null;
    		for (int i = 0; i < pt.length; i++) {
    			if (np == null) {
    				np = new StringBuilder();
    				np.append(pt[i]);
    			} else {
    				np.append("/" + pt[i]);
    			}
    			
    		}
    		path = np.toString();
    	}
    	if (!path.startsWith(".")) {
    		path = "." + path;
    	}
    	if (!name.endsWith(".yml")) {
    		name = name + ".yml";
    	}
    	this.path = path;
    	this.fileName = name;
    	File file = new File(path + File.separator + name);
    	fileConfigFile = file;
		fileConfig = YamlConfiguration.loadConfiguration(file);
	}
	
	//Returns the FileConfiguration, so you can add/change stuff in the config
	public FileConfiguration getFC() {
		return fileConfig;
	}
	public void save(FileConfiguration fc) {
		try {
			fc.save(fileConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Saves, simple
	public void save() {
		try {
			fileConfig.save(fileConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Returns ALL player configs.
	public static List<String> listPlayerConfigs() {
		List<String> ret = new ArrayList<String>();
		File dir = new File("./plugins/PalCraftEssentials/players");
		for (String x : dir.list()) {
			String p = x.split(".yml")[0];
			ret.add(p.toLowerCase());
		}
		
		return ret;
	}
}
