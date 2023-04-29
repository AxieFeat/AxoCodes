package ru.axiefeat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.axiefeat.Commands.CodeCmd;
import ru.axiefeat.Commands.AdminCmd;

public final class Main extends JavaPlugin {

    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    public Main() {
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        instance = this;
        saveDefaultConfig();
        Utils.createCustomConfig();
        if (Utils.storage.getConfigurationSection("codes") == null) {
            Utils.storage.createSection("codes");
            Utils.reloadStorage();
        }
        this.getCommand("axocode").setExecutor(new AdminCmd());
        this.getCommand("code").setExecutor(new CodeCmd());
        Utils.Logger("Loaded " + getConfig().getConfigurationSection("codes").getKeys(false).size() + " codes.");
    }

    @Override
    public void onDisable() {
        Utils.saveStorage();
    }
}
