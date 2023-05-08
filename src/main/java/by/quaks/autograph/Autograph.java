package by.quaks.autograph;

import org.bukkit.plugin.java.JavaPlugin;

public final class Autograph extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        AphList.setup();
        getCommand("autograph").setExecutor(new AutographCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
