package by.quaks.main.listeners;

import by.quaks.main.AutographPlugin;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class AnvilListener implements Listener {
    private AutographPlugin plugin;
    public AnvilListener(AutographPlugin plugin){
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
        Player player = (Player)event.getView().getPlayer();
        AnvilInventory inventory = event.getInventory();
        if(previousInventories.put(player, inventory) == null) {
            onPrepareAnvil(event);
            getServer().getScheduler().runTask(plugin, () -> previousInventories.remove(player));
        }
    }
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        System.out.println("1");
    }
}
