package by.quaks.main;

import by.quaks.main.commands.AutographCommand;
import by.quaks.main.commands.RMAutographCommand;
import by.quaks.main.config.ConfigsInitializer;
import by.quaks.main.listeners.AnvilListener;
import by.quaks.main.listeners.GrindstoneListener;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class AutographPlugin extends JavaPlugin {

    private BukkitAudiences adventure;
    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable(){
        this.adventure = BukkitAudiences.create(this);
        new ConfigsInitializer();
        getCommand("autograph").setExecutor(new AutographCommand(adventure()));
        getCommand("remove-autograph").setExecutor(new RMAutographCommand(adventure()));
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new GrindstoneListener(), this);
    }
    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}
