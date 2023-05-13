package by.quaks.autograph;

import org.bukkit.plugin.java.JavaPlugin;

public final class Autograph extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!this.getDataFolder().exists()) { // Создание папки для хранения конфигурационных файлов
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Config.setup();
        if(Config.get().getBoolean("command")) {
            getCommand("autograph").setExecutor(new AutographCMD());
        }
        if(Config.get().getBoolean("anvil")) {
            getServer().getPluginManager().registerEvents(new AnvilCraft(), this);
        }
        getCommand("cleara").setExecutor(new ClearCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
