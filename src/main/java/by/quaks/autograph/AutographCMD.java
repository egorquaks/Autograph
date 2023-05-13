package by.quaks.autograph;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AutographCMD implements CommandExecutor {
    List<String> autographItemList = Config.get().getStringList("autographItemList");
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            assert p != null;
            ItemStack item = p.getItemInHand();
            if (item.getType() == null || item.getType().toString().contains("AIR")) {
                p.sendMessage(Config.get().getString("itemUndefined"));
            } else if (!autographItemList.contains(item.getType().toString())) {
                p.sendMessage(ChatColor.RED + Config.get().getString("itemNotTheList"));
                return true;
            } else {
                if(!Config.get().getBoolean("multiAutographs")){
                    if (Utils.hasAutograph(item)) {
                        p.sendMessage(ChatColor.RED + Config.get().getString("itemContainsMaxAutographs"));
                        return true;
                    }
                }
                if(Utils.hasAutographBy(item,p.getName())){
                    p.sendMessage(ChatColor.RED + Config.get().getString("itemContainsMaxAutographsBy"));
                    return true;
                }
                Utils.setLore(item,Utils.genAutograph(p.getName()));
            }
        }
        return true;
    }
}
