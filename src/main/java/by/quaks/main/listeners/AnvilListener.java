package by.quaks.main.listeners;

import by.quaks.main.AutographPlugin;
import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class AnvilListener implements Listener {
    private AutographPlugin plugin;

    public AnvilListener(AutographPlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, AnvilInventory> previousInventories = new HashMap<>();

    @EventHandler
    public void onPrepareAnvilFix(PrepareAnvilEvent event) {
        /*
         * Такая реализация ивента предотвращает множественные вызовы ивента из-за бага внутри Spigot который просто
         * по рофлу вызывает этот метод по 3-5 раз.
         * Самое удивительное, что фиксить это никто не собирается... ну, ладно.
         */
        Player player = (Player) event.getView().getPlayer();
        AnvilInventory inventory = event.getInventory();
        if (previousInventories.put(player, inventory) == null) {
            onPrepareAnvil(event);
            getServer().getScheduler().runTask(plugin, task -> previousInventories.remove(player));
        }
        onPrepareAnvil(event);
    }

    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!MainConfig.get().getBoolean("anvil.enabled")) {
            return;
        }
        ItemStack baseItem = event.getInventory().getItem(0);
        ItemStack appliedItem = event.getInventory().getItem(1);
        if (baseItem == null || appliedItem == null) {
            return;
        }
        ItemStack finalItem = baseItem.clone();
        List<String> allowedItems = MainConfig.get().getStringList("allowedItems");
        if(appliedItem.getType().name().equals(MainConfig.get().get("anvil.item.type"))){
            if(MainConfig.get().getBoolean("anvil.item.useItemName")) {
                Objects.requireNonNull(appliedItem.getItemMeta()).getDisplayName();
                if(appliedItem.getItemMeta().getDisplayName().equals(MainConfig.get().getString("anvil.item.itemName"))){
                    applyAutograph(event, baseItem, finalItem, allowedItems);
                }
            }else{
                applyAutograph(event, baseItem, finalItem, allowedItems);
            }
        }
    }

    private void applyAutograph(PrepareAnvilEvent event, ItemStack baseItem, ItemStack finalItem, List<String> allowedItems) {
        if (allowedItems.stream().anyMatch(baseItem.getType().toString()::equalsIgnoreCase)) {
            boolean hasAutographBy = Utils.hasAutographBy(baseItem, event.getView().getPlayer().getName());
            if (!hasAutographBy) {
                Utils.setLore(finalItem, Utils.genAutograph(event.getView().getPlayer().getName()));
                event.setResult(finalItem);
                setRepairCost(event, MainConfig.get().getInt("anvil.autographCost"));
                return;
            }
            if (MainConfig.get().getBoolean("general.multipleAutographs")) {
                Utils.setLore(finalItem, Utils.genAutograph(event.getView().getPlayer().getName()));
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
