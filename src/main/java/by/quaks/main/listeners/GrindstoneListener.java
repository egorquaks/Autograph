package by.quaks.main.listeners;

import by.quaks.main.AutographPlugin;
import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GrindstoneListener implements Listener {
    private AutographPlugin plugin;

    public GrindstoneListener(AutographPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    private void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory() instanceof GrindstoneInventory) {
            Inventory grindstoneInventory = event.getInventory();
            int rawSlot = event.getRawSlot();
            if (rawSlot == 2 && (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
                    || event.getAction().equals(InventoryAction.PICKUP_ALL)
                    || event.getAction().equals(InventoryAction.PICKUP_HALF)
                    || event.getAction().equals(InventoryAction.PICKUP_ONE)
            )) {
                ItemStack slot1 = event.getInventory().getItem(0);
                ItemStack slot2 = event.getInventory().getItem(1);
                if ((slot1 != null && Utils.isAutographed(slot1))
                        || (slot2 != null && Utils.isAutographed(slot2))){
                    plugin.getServer().getScheduler().runTask(plugin, () ->{
                        boolean inputsEmpty = grindstoneInventory.getItem(0) == null
                                && grindstoneInventory.getItem(1) == null;
                        int refund = MainConfig.get().getInt("anvil.autographCost") / 4;
                        if(inputsEmpty){
                            Location location = grindstoneInventory.getLocation();
                            if(location != null && location.getWorld() != null && refund > 0){
                                location.getWorld().spawn(
                                        location.clone().add(0.5,1,0.5),
                                        ExperienceOrb.class,
                                        orb -> orb.setExperience(refund)
                                );
                            }
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    private void onPrepareGrindstone(PrepareGrindstoneEvent event) {
        if (MainConfig.get().getBoolean("remove-autograph.grindstone.enabled")) {
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
            ItemMeta itemMeta = finalItem.getItemMeta();
            itemMeta.setLore(Utils.clearedLore(itemMeta.getLore()));
            finalItem.setItemMeta(itemMeta);
            event.setResult(finalItem);
        }
    }
}
