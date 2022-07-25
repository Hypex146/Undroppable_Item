package plugin.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import plugin.UndroppableItem;
import plugin.tasks.Tasks;
import plugin.utilities.LogLevel;

public class EventListener implements Listener {
	private UndroppableItem main_plugin_;
	
	public EventListener(UndroppableItem main_plugin) {
		main_plugin_ = main_plugin;
	}
	
	private boolean isPartlyUndroppable(ItemStack itemStack) {
		if (itemStack==null) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.WARNING, "ItemStack==null");
			return false; 
			}
		ItemMeta item_meta = itemStack.getItemMeta();
		if (item_meta==null) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.WARNING, "ItemMeta==null");
			return false; 
			}
		String undroppable_mod = 
				item_meta.getPersistentDataContainer().get(main_plugin_.undroppable_mod_key_, 
						PersistentDataType.STRING);
		if (undroppable_mod != null && undroppable_mod.equals(main_plugin_.getPartlyUndroppableTag())) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.INFO, "Ёто частично невыбрасываемый предмет");
			return true; 
			}
		return false;
	}
	
	private boolean isFullyUndroppable(ItemStack itemStack) {
		if (itemStack==null) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.WARNING, "ItemStack==null");
			return false; 
			}
		ItemMeta item_meta = itemStack.getItemMeta();
		if (item_meta==null) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.WARNING, "ItemMeta==null");
			return false; 
			}
		String undroppable_mod = 
				item_meta.getPersistentDataContainer().get(main_plugin_.undroppable_mod_key_, 
						PersistentDataType.STRING);
		if (undroppable_mod != null && undroppable_mod.equals(main_plugin_.getFullyUndroppableTag())) { 
			main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.INFO, "Ёто полностью невыбрасываемый предмет");
			return true; 
			}
		return false;
	}
	
	@EventHandler
	public void onDropItemFromPlayer(PlayerDropItemEvent event) {
//		main_plugin_.getUILogger().log(LogLevel.DEBUG, Level.INFO, event.getPlayer().getName() + 
//				" выбросил " + event.getItemDrop().getItemStack().toString());
		if (event.getPlayer().getGameMode()==GameMode.CREATIVE) { return; }
		ItemStack item_stack = event.getItemDrop().getItemStack();
		if (isFullyUndroppable(item_stack)) {
			event.setCancelled(true);
		}
		return;
	}
	
	@EventHandler
	public void onDeathPlayer(PlayerDeathEvent event) {
		List<ItemStack> item_stacks = event.getDrops();
		List<ItemStack> item_stacks_for_remove = new ArrayList<ItemStack>();
		Player player = event.getEntity();
		for (ItemStack item_stack : item_stacks) {
			if (isFullyUndroppable(item_stack) ||
					isPartlyUndroppable(item_stack)) {
				item_stacks_for_remove.add(item_stack);
			}
		}
		item_stacks.removeAll(item_stacks_for_remove);
		Bukkit.getServer().getScheduler().runTaskLater(
				Bukkit.getServer().getPluginManager().getPlugin("Undroppable_item"), new Runnable(){
					public void run() {
						Tasks.addItemToInventory(item_stacks_for_remove, player);
					}
				}, 1);
		return;
	}

	@EventHandler
	public void onTakeItemInCursor(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode()==GameMode.CREATIVE) { return; }
        Inventory topInventory = event.getView().getTopInventory();
        Inventory bottomInventory = event.getView().getBottomInventory();
        if (bottomInventory.getType()!=InventoryType.PLAYER) { return; }
        if (topInventory.getType()==InventoryType.CRAFTING) { return; }
        ItemStack itemStack = event.getCurrentItem();
        if (isFullyUndroppable(itemStack)) {
        	event.setCancelled(true);
        }
        return;
	}
	
}
