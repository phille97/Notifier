package io.github.phille97.notifier;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerHandler {
	
	private UUID uuid;
	private ArrayList<String> words;
	
	public PlayerHandler(Player player){
		this.words = new ArrayList<String>();
		this.uuid = player.getUniqueId();
		// Enable
		
		notify(2);
	}
	
	public void disable(){
		// Disable
	}
	
	public String getPlayerName(){
		return Bukkit.getPlayer(uuid).getName();
	}
	
	public void notify(int times){
		for(int i=0; i < times; i++){
			if(getMe() != null){
				getMe().playSound(getMe().getLocation(), Sound.CLICK, 1, 1);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public String[] getWords(){
		return (String[]) words.toArray();
	}
	
	public void addWord(String word){
		if(!words.contains(word)){
			words.add(word);
		}
	}
	
	public void removeWord(String remword){
		ArrayList<String> to_remove = new ArrayList<String>();
		for(String word : words){
			if(word.equalsIgnoreCase(remword));
			to_remove.add(word);
		}
		for(String rem : to_remove){
			words.remove(rem);
		}
	}
	
	public void clearList(){
		words.clear();
	}
	
	public boolean isAdmin(){
		return getMe().hasPermission("notifier.admin");
	}
	
	private Player getMe(){
		if(Bukkit.getPlayer(uuid) != null){
			return Bukkit.getPlayer(uuid);
		}
		return null;
	}
}
