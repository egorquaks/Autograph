package by.quaks.autograph;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void setLore(ItemStack item, String lore_text){
        ItemMeta itemM = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemM != null;
        if (itemM.getLore() != null) {
            lore = itemM.getLore();
        }
        lore.add(lore_text);
        itemM.setLore(lore);
        item.setItemMeta(itemM);
    }
    public static boolean hasAutograph(ItemStack item){
        ItemMeta itemM = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemM != null;
        if (itemM.getLore() != null) {
            lore = itemM.getLore();
        }
        return lore.toString().contains(Config.get().getString("autographForm").replaceAll("\\{player-name}", ""));
    }
    public static String getItemName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            return ChatColor.stripColor(displayName);
        }

        return null;
    }
    public static String genAutograph(String author){
       return ChatColor.GRAY + Config.get().getString("autographForm").replaceAll("\\{player-name}",ChatColor.AQUA+author+ChatColor.GRAY);
    }
    public static boolean isAutographable(ItemStack item){
        List<String> autographItemList = Config.get().getStringList("autographItemList");
        return autographItemList.contains(item.getType().toString());
    }
}
