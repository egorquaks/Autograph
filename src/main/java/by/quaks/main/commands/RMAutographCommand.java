package by.quaks.main.commands;

import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RMAutographCommand implements CommandExecutor {
    private final BukkitAudiences adventure;
    public RMAutographCommand(BukkitAudiences adventure){
        this.adventure = adventure;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player){
            if(MainConfig.get().getBoolean("remove-autograph.command.enabled")){
                ItemStack item = player.getInventory().getItemInMainHand();
                if(Utils.isAutographed(item)){
                    ItemMeta itemMeta = item.getItemMeta();
                    List<String> lore = itemMeta.getLore();
                    itemMeta.setLore(Utils.clearedLore(lore));
                    item.setItemMeta(itemMeta);
                }
            }else{
                sendMessageToPlayer(player,"remove-autograph.command.disabledMessage");
            }
        }
        return true;
    }
    private void sendMessageToPlayer(Player player, String path) {
        Component message = MiniMessage.miniMessage().deserialize(MainConfig.get().getString(path));
        adventure.player(player).sendMessage(message);
    }
}
