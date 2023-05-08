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
        AphList.setup();
        getCommand("autograph").setExecutor(new AutographCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
