package by.quaks.autograph;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClearCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            Player p = ((Player) commandSender).getPlayer();
            assert p != null;
            if (p.hasPermission("autograph.clear"))
            {
                ItemStack item = p.getItemInHand();
                if (item.getType() == null || item.getType().toString().contains("AIR")) {
                    p.sendMessage(Config.get().getString("itemUndefined"));
                }
                if (Utils.hasAutograph(item)) {
                    Utils.clearAutograph(item);
                    return true;
                }
            }
        }
        return true;
    }
}
