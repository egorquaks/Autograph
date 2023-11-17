package by.quaks.main.commands;

import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AutographCommand implements CommandExecutor {
    private final BukkitAudiences adventure;
    public AutographCommand(BukkitAudiences adventure){
        this.adventure = adventure;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player){
            if(MainConfig.get().getBoolean("command.enabled")){
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType().isAir()) {
                    sendMessageToPlayer(player,"general.emptyHandMessage");
                    return true;
                }
                List<String> allowedItems = MainConfig.get().getStringList("general.allowedItems");
                allowedItems.forEach(String::toLowerCase);
                if (allowedItems.contains(item.getType().name().toLowerCase())) {
                    sendMessageToPlayer(player,"general.itemNotTheList");
                    return true;
                } else {
                    boolean hasAutographBy = Utils.hasAutographBy(item, player.getName());
                    if (!hasAutographBy) {
                        Utils.addLore(item, Utils.genAutograph(player.getName()));
                        return true;
                    } else {
                        sendMessageToPlayer(player,"general.itemContainsMaxAutographsBy");
                    }
                    if (MainConfig.get().getBoolean("general.multipleAutographs")) {
                        Utils.addLore(item, Utils.genAutograph(player.getName()));
                        return true;
                    }
                }
            }else{
                sendMessageToPlayer(player,"command.disabledMessage");
            }
        }
        return true;
    }
    private void sendMessageToPlayer(Player player, String path) {
        Component message = MiniMessage.miniMessage().deserialize(MainConfig.get().getString(path));
        adventure.player(player).sendMessage(message);
    }
}
