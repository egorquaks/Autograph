package by.quaks.main.listeners;

import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GrindstoneListener implements Listener {
    @EventHandler
    private void onPrepareGrindstone(PrepareGrindstoneEvent event) {
        if(MainConfig.get().getBoolean("remove-autograph.grindstone.enabled")){
            ItemStack upperItem = event.getInventory().getItem(0);
            ItemStack bottomItem = event.getInventory().getItem(1);
            ItemStack finalItem;
            if (upperItem != null && bottomItem == null) {
                if (!Utils.isAutographed(upperItem)) {
                    return;
                }
                finalItem = upperItem.clone();
            } else if (upperItem == null && bottomItem != null) {
                if (!Utils.isAutographed(bottomItem)) {
                    return;
                }
                finalItem = bottomItem.clone();
            } else {
                return;
            }
            ItemMeta itemMeta = upperItem.getItemMeta();
            itemMeta.setLore(Utils.clearedLore(itemMeta.getLore()));
            finalItem.setItemMeta(itemMeta);
            event.setResult(finalItem);
        }
    }
}
