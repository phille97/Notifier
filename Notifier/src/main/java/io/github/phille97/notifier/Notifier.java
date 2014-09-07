package io.github.phille97.notifier;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Notifier extends JavaPlugin {
	public static final String PLUGIN_VERSION = "0.1.0";
	public static final String PLUGIN_NAME = "Notifier";
	public static boolean mention_user_default = false; // TODO Use me!
	public static boolean notifier_useperms = false; // TODO Use me!
	public static String[] admin_words; // TODO Use me!
	private ArrayList<PlayerHandler> players = new ArrayList<PlayerHandler>();
	
	@Override
    public void onEnable() {
		showConsole("Thank you for using my plugin! :D  // Phille97");
		getServer().getPluginManager().registerEvents(new ChatChecker(), this);
		try {
			this.loadConfig();
		} catch (InvalidConfigException e) {
			this.getLogger().warning(e.getMessage());
		}
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    private void loadConfig() throws InvalidConfigException{
    	this.saveDefaultConfig();
    	this.reloadConfig();
    	mention_user_default = this.getConfig().getBoolean("mention_user_default");
    	notifier_useperms = this.getConfig().getBoolean("notifier_useperms");
    	if(this.getConfig().getBoolean("admin_words_use")){
    		admin_words = (String[]) this.getConfig().getStringList("admin_words").toArray();
    	}
    	if(this.getConfig().getInt("version") != 1){
    		throw new InvalidConfigException("Wrong config version! Delete the config.yml to regenerate it.");
    	}
    }
    
    // TODO Add commands to control multiple words and some config thingies
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("notifier")) { // The menu and plugin info
    		String[] say = {
    				"/-------------------------/",
    				"-" + PLUGIN_NAME + " is running version " + PLUGIN_VERSION,
    				"- Commands:",
    				"-  /notifier  -  Shows this",
    				"-  /notify    -  Toggle notifications",
    				"/-------------------------/"};
    		sender.sendMessage(say);
    		return true;
    	}
    	if (cmd.getName().equalsIgnoreCase("notify")) { // enable and disable notifications
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    		} else {
    			Player player = (Player) sender;
    			PlayerHandler todisable = null;
    			for(PlayerHandler ph : players){
    				if(ph.getPlayerName().equals(player.getName())){
    					todisable = ph; // Found a player thread already running
    				}
    			}
    			if(todisable != null){
    				// Disable it and remove it from the ArrayList
    				todisable.disable();
    				players.remove(todisable);
    				todisable.notify(1);
    				todisable = null;
    				player.sendMessage(PLUGIN_NAME + " disabled!");
    				showConsole("Disabled player: " + player.getName());
    				return true;
    			}
    			// Enable
    			PlayerHandler ph = new PlayerHandler(player);
    			players.add(ph);
    			player.sendMessage(PLUGIN_NAME + " enabled!");
    			showConsole("Enabled player: " + player.getName());
    		}
    		return true;
    	}
    	if(cmd.getName().equalsIgnoreCase("nudge")){
    		if(args.length != 1) return false;
    		String name = args[0];
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("Notifying player.");
    		} else {
    			Player player = (Player) sender;
    			player.sendMessage("This feature is only avavible in the console!");
    		}
    		return true;
    	}
    	return false; 
    }
    
    public String[] getEnabledUsers(){
    	ArrayList<String> list = new ArrayList<String>();
    	for(PlayerHandler ph : players){
    		list.add(ph.getPlayerName());
    	}
    	String[] hej = new String[list.size()];
    	for(int i = 0; i < list.size(); i++){
    		hej[i] = list.get(i);
    	}
    	return hej;
    }
    
    private void showConsole(String text){
    	getLogger().info(text);
    }
    
    // Notifies the specified player.
    public void notifyPlayer(String playername){
    	for(PlayerHandler player : players){
    		if(player.getPlayerName().equalsIgnoreCase(playername)){
    			player.notify(1);
    		}
    	}
    }
    
    public void notifyAdmins(){
    	for(PlayerHandler player : players){
    		if(player.isAdmin()){
    			player.notify(1);
    		}
    	}
    }
    
    public class ChatChecker implements Listener {
    	
    	@EventHandler(priority = EventPriority.MONITOR)
    	public void onPlayerChat(AsyncPlayerChatEvent event){
    		String playername = event.getPlayer().getDisplayName();
    		String message = event.getMessage();
    		String[] players = Notifier.this.getEnabledUsers();
    		for(String p : players){
    			int i = message.indexOf(p);
    			if(i == -1){
    				// do not contain
    				continue;
    			}
    			if(p.equalsIgnoreCase(playername)){
    				// This player typed his own name. Good job!
    				continue;
    			}
    			notifyPlayer(p);
    			break;
    		}
    	}
    }
}
