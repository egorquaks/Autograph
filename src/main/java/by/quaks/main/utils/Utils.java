package by.quaks.main.utils;

import by.quaks.main.config.MainConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
        String autographFormRaw = MainConfig.get().getString("general.autograph");
        Component component = MiniMessage.miniMessage().deserialize(autographFormRaw);
        String autographForm = PlainTextComponentSerializer.plainText().serialize(component);
        String autographPattern = autographForm.replaceAll("\\{player-name}","(.*?)");
        Pattern pattern = Pattern.compile(autographPattern);
        return lore.stream().anyMatch(s -> {
            return pattern.matcher(ChatColor.stripColor(s)).find();
        });
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
    public static void addLore(ItemStack item, String lore_text){
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
        //Bukkit.getLogger().info(GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(miniAutograph)));
        return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(MiniMessage.miniMessage().deserialize(miniAutograph));
    }
    public static boolean canContainAutograph(ItemStack item){
        List<String> autographItemList = MainConfig.get().getStringList("allowedItems");
        return autographItemList.contains(item.getType().name());
    }
    public static String getItemName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            return ChatColor.stripColor(displayName);
        }

        return null;
    }
    public static List<String> getLore(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasLore()) {
            return meta.getLore();
        }
        return new ArrayList<>(); // Возвращаем пустой список, если Lore отсутствует
    }
    public static List<String> clearedLore(List<String> lore) {
        String autographFormRaw = MainConfig.get().getString("general.autograph");
        Component component = MiniMessage.miniMessage().deserialize(autographFormRaw);
        String autographForm = PlainTextComponentSerializer.plainText().serialize(component);
        String autographPattern = autographForm.replaceAll("\\{player-name}","(.*?)");
        Pattern pattern = Pattern.compile(autographPattern);
        return filterLore(lore,pattern);
    }
    public static List<String> filterLore(List<String> lore, Pattern pattern) {
//        lore.forEach(s -> {
//            Bukkit.getLogger().info(pattern.pattern());
//            Bukkit.getLogger().info(ChatColor.stripColor(s));
//        });
        lore.removeIf(s -> pattern.matcher(ChatColor.stripColor(s)).find());
        return lore;
    }

}
