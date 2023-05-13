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
    public static void clearAutograph(ItemStack item){
        ItemMeta itemM = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemM != null;
        if (itemM.getLore() != null) {
            lore = itemM.getLore();
        }
        lore.removeIf(element -> element.contains(Config.get().getString("autographForm").replaceAll("\\{player-name}","")));
        itemM.setLore(lore);
        item.setItemMeta(itemM);
    }

    public static boolean hasAutographBy(ItemStack item, String name){
        ItemMeta itemM = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemM != null;
        if (itemM.getLore() != null) {
            lore = itemM.getLore();
        }
        return lore.stream().anyMatch(element -> element.contains(name));
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
       return ChatColor.GRAY + Config.get().getString("autographForm").replaceAll("\\{player-name}",getColor(Config.get().getString("playerNameColor"))+author+ChatColor.GRAY);
    }
    public static boolean isAutographable(ItemStack item){
        List<String> autographItemList = Config.get().getStringList("autographItemList");
        return autographItemList.contains(item.getType().toString());
    }
    private static ChatColor getColor(String text) {
        if (text.startsWith("#")) {
            return ChatColor.of(text);
        } else {
            switch (text.toUpperCase()) {
                case "BLACK":
                    return ChatColor.BLACK;
                case "DARK_BLUE":
                    return ChatColor.DARK_BLUE;
                case "DARK_GREEN":
                    return ChatColor.DARK_GREEN;
                case "DARK_AQUA":
                    return ChatColor.DARK_AQUA;
                case "DARK_RED":
                    return ChatColor.DARK_RED;
                case "DARK_PURPLE":
                    return ChatColor.DARK_PURPLE;
                case "GOLD":
                    return ChatColor.GOLD;
                case "GRAY":
                    return ChatColor.GRAY;
                case "DARK_GRAY":
                    return ChatColor.DARK_GRAY;
                case "BLUE":
                    return ChatColor.BLUE;
                case "GREEN":
                    return ChatColor.GREEN;
                case "AQUA":
                    return ChatColor.AQUA;
                case "RED":
                    return ChatColor.RED;
                case "LIGHT_PURPLE":
                    return ChatColor.LIGHT_PURPLE;
                case "YELLOW":
                    return ChatColor.YELLOW;
                case "WHITE":
                    return ChatColor.WHITE;
                default:
                    return ChatColor.AQUA;
            }
        }
    }

}
