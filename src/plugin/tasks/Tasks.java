package plugin.tasks;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Tasks {
	
	public static void addItemToInventory(List<ItemStack> itemStackList, Player player) {
		if (itemStackList==null) {return;}
		for (ItemStack itemStack : itemStackList) {
			player.getInventory().addItem(itemStack);
		}
		return;
	}

}
