package by.quaks.main.commands;

import by.quaks.main.config.MainConfig;
import by.quaks.main.utils.Utils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item.getType().isAir()){
                player.sendRawMessage("");//minimessage
                return true;
            }
            List<String> allowedItems = MainConfig.get().getStringList("allowedItems");
            if(allowedItems.stream().noneMatch(item.getType().toString()::equalsIgnoreCase)){
               player.sendMessage("This item cannot be autographed"); //minimessage
               return true;
            }else{
                boolean hasAutographBy = Utils.hasAutographBy(item, player.getName());
                    if (!hasAutographBy) {
                        Utils.setLore(item, Utils.genAutograph(player.getName()));
                        return true;
                    }else{
                        adventure.player(player).sendMessage( MiniMessage.miniMessage().deserialize(MainConfig.get().getString("general.itemContainsMaxAutographsBy")));
                    }
                    if(MainConfig.get().getBoolean("general.multipleAutographs")){
                        Utils.setLore(item, Utils.genAutograph(player.getName()));
                        return true;
                    }
            }
        }
        return true;
    }
}
