package by.quaks.main.listeners;

import by.quaks.main.AutographPlugin;
import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class AnvilListener implements Listener {
    private AutographPlugin plugin;

    public AnvilListener(AutographPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!MainConfig.get().getBoolean("anvil.enabled")) {
            return;
        }
        ItemStack baseItem = event.getInventory().getItem(0);
        ItemStack appliedItem = event.getInventory().getItem(1);
        if (baseItem == null || appliedItem == null || !Utils.canContainAutograph(baseItem)) {
            return;
        }
        ItemStack finalItem = baseItem.clone();
        if(appliedItem.getType().name().equals(MainConfig.get().get("anvil.item.type"))){
            if(MainConfig.get().getBoolean("anvil.item.useItemName")) {
                if(Objects.equals(Utils.getItemName(appliedItem), MainConfig.get().getString("anvil.item.itemName"))){
                    applyAutograph(event, baseItem, finalItem);
                }
            }else{
                applyAutograph(event, baseItem, finalItem);
            }
        }
    }

    private void applyAutograph(PrepareAnvilEvent event, ItemStack baseItem, ItemStack finalItem) {
        if (Utils.canContainAutograph(baseItem)) {
            boolean hasAutographBy = Utils.hasAutographBy(baseItem, event.getView().getPlayer().getName());
            if (!hasAutographBy) {
                Utils.addLore(finalItem, Utils.genAutograph(event.getView().getPlayer().getName()));
                event.setResult(finalItem);
                setRepairCost(event, MainConfig.get().getInt("anvil.autographCost"));
                return;
            }
            if (MainConfig.get().getBoolean("general.multipleAutographs")) {
                Utils.addLore(finalItem, Utils.genAutograph(event.getView().getPlayer().getName()));
                event.setResult(finalItem);
                setRepairCost(event, MainConfig.get().getInt("anvil.autographCost"));
            }
        }
    }

    private void setRepairCost(PrepareAnvilEvent event, int cost) {
        getServer().getScheduler().runTask(plugin, () -> {
            event.getInventory().setRepairCost(cost);
            for (HumanEntity viewer : event.getInventory().getViewers()) {
                if (viewer instanceof Player) {
                    viewer.setWindowProperty(InventoryView.Property.REPAIR_COST, cost);
                }
            }
        });
    }
}
