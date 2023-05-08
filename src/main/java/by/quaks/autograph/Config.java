package by.quaks.autograph;

import org.bukkit.configuration.file.FileConfiguration;

public interface Config {
    static void setup() {   }
    static FileConfiguration get(){return null;}
    static void save(){  }
    public static void reload(){    }
}
