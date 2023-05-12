package by.quaks.autograph;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class AnvilCraft implements Listener {
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack firstItem = event.getInventory().getItem(0);
        ItemStack secondItem = event.getInventory().getItem(1);
        if (
                firstItem != null
                && secondItem != null
                && !Utils.hasAutograph(firstItem)
                && Utils.isAutographable(firstItem)
                && Objects.equals(secondItem.getType().toString(), Config.get().getString("autographItem"))
                && Objects.equals(Objects.requireNonNull(Utils.getItemName(secondItem)).toLowerCase(), Objects.requireNonNull(Config.get().getString("autographItemName")).toLowerCase())
        ){
            ItemStack result = firstItem.clone();
            Utils.setLore(result, Utils.genAutograph(event.getView().getPlayer().getName()));
            event.setResult(result);
            event.getInventory().setRepairCost(Config.get().getInt("autographCost"));
        }
    }
}
