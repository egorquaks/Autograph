package by.quaks.main.utils;

import by.quaks.autograph.Config;
import by.quaks.main.config.MainConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isAutographed(ItemStack itemStack){
        List<String> lore = getLore(itemStack);
        String autographForm = MainConfig.get().getString("general.autograph");
        String autographPattern = autographForm.replaceAll("\\{player-name}","(.*?)");
        Pattern pattern = Pattern.compile(autographPattern);
        return lore.stream().anyMatch(s -> pattern.matcher(ChatColor.stripColor(s)).find());
    }
    public static boolean hasAutographBy(ItemStack itemStack, String playerName){
        List<String> lore = getLore(itemStack);
        String playerPattern = Pattern.quote(playerName);
        Pattern pattern = Pattern.compile(playerPattern, Pattern.CASE_INSENSITIVE);
        // Проверяем, есть ли упоминание игрока в каждой строке из списка lore
        if(lore==null){return false;}
        return lore.stream().anyMatch(s -> {
            Matcher matcher = pattern.matcher(ChatColor.stripColor(s));
            return matcher.find();
        });
    }
    public static void setLore(ItemStack item, String lore_text){
        ItemMeta itemM = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert itemM != null;
        if (itemM.getLore() != null) {
            lore = itemM.getLore();
        }
        for(String s : lore){
            Bukkit.getLogger().info("lore:"+s);
        }
        lore.add(lore_text);
        itemM.setLore(lore);
        item.setItemMeta(itemM);
    }
    public static String genAutograph(String author){
        String miniAutograph = MainConfig.get().getString("general.autograph").replaceAll("\\{player-name}",author);
        Bukkit.getLogger().info(GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(miniAutograph)));
        return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(MiniMessage.miniMessage().deserialize(miniAutograph));
    }
    public static boolean canContainAutograph(ItemStack item){
        List<String> autographItemList = Config.get().getStringList("autographItemList");
        return autographItemList.contains(item.getType().toString());
    }
    public static String getItemName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            return ChatColor.stripColor(displayName);
        }

        return null;
    }
    private static List<String> getLore(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(itemMeta!=null) {lore = itemMeta.getLore();}
        return lore;
    }
}
