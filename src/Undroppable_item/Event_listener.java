package Undroppable_item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Event_listener implements Listener {
	private String unDroppableAfterDeathLore;
	private String blockedInInventoryLore;
	
	public Event_listener(String blacklistLore, String blocked) {
		this.unDroppableAfterDeathLore = blacklistLore;
		this.blockedInInventoryLore = blocked;
	}
	
	public void setUnDroppableAfterDeathLore(String unDroppableAfterDeathLore) {
		this.unDroppableAfterDeathLore = unDroppableAfterDeathLore;
	}
	
	public String getUnDroppableAfterDeathLore() {
		return this.unDroppableAfterDeathLore;
	}
	
	public void setBlockedInInventoryLore(String blockedInInventory) {
		this.blockedInInventoryLore = blockedInInventory;
	}
	
	public String getBlockedInInventoryLore() {
		return this.blockedInInventoryLore;
	}
	
	private boolean isUnDroppableAfterDeath(ItemStack itemStack) {
		if (itemStack==null) {return false;}
		if (itemStack.getItemMeta()==null) {return false;}
		if (itemStack.getItemMeta().getLore()==null) {return false;}
		List<String> loreList = itemStack.getItemMeta().getLore();
		for (String lore : loreList) {
			if (lore.contains(unDroppableAfterDeathLore)) {return true;}
		}
		return false;
	}
	
	private boolean isblockedInInventory(ItemStack itemStack) {
		if (itemStack==null) {return false;}
		if (itemStack.getItemMeta()==null) {return false;}
		if (itemStack.getItemMeta().getLore()==null) {return false;}
		List<String> loreList = itemStack.getItemMeta().getLore();
		for (String lore : loreList) {
			if (lore.contains(blockedInInventoryLore)) {return true;}
		}
		return false;
	}
	
	@EventHandler
	public void onDropItemFromPlayer(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode()==GameMode.CREATIVE) {return;}
		ItemStack itemStack = event.getItemDrop().getItemStack();
		if (isblockedInInventory(itemStack)) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler
	public void onDeathPlayer(PlayerDeathEvent event) {
		List<ItemStack> itemStackList = event.getDrops();
		List<ItemStack> itemStackListForRemove = new ArrayList<ItemStack>();
		Player player = event.getEntity();
		for (ItemStack itemStack : itemStackList) {
			if (isUnDroppableAfterDeath(itemStack) ||
					isblockedInInventory(itemStack)) {
				itemStackListForRemove.add(itemStack);
			}
		}
		itemStackList.removeAll(itemStackListForRemove);
		Bukkit.getServer().getScheduler().runTaskLater(
				Bukkit.getServer().getPluginManager().getPlugin("Undroppable_item"), new Runnable(){
					public void run() {
						MyScheduler.addItemToInventory(itemStackListForRemove, player);
					}
				}, 1);
		return;
	}

	@EventHandler
	public void onTakeItemInCursor(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode()==GameMode.CREATIVE) {return;}
        Inventory topInventory = event.getView().getTopInventory();
        Inventory bottomInventory = event.getView().getBottomInventory();
        if (bottomInventory.getType()!=InventoryType.PLAYER) {return;}
        if (topInventory.getType()==InventoryType.CRAFTING) {return;}
        ItemStack itemStack = event.getCurrentItem();
        if (isblockedInInventory(itemStack)) {
        	event.setCancelled(true);
        }
        return;
	}
	
}
