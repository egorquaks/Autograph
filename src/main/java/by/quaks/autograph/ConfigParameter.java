package by.quaks.autograph;

import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.stream.IntStream;

//TODO: ЗАДЕЛКИ НА БУДУЩЕЕ
public class ConfigParameter<T> {
    private final T value;
    public ConfigParameter(T value) {
        this.value = value;
    }
    public T getValue() {
        if (value == null || value.toString().isEmpty()) {
            IntStream.range(0, 3).forEach(i -> { Bukkit.getLogger().warning("There is an empty parameter in the config that can cause errors. Plugin stopped avoiding more serious bugs."); });
            Bukkit.getServer().getPluginManager().disablePlugin(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Autograph")));
        }
        return value;
    }
}
