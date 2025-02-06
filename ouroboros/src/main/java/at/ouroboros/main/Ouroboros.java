package at.ouroboros.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Ouroboros extends JavaPlugin {

    private static Ouroboros PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLeftEvent(), this);
        this.getCommand("markt").setExecutor(new CmdMarkt());
    }

    public static Ouroboros getPlugin() {
        return PLUGIN;
    }
}
