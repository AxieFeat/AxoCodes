package ru.axiefeat;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.*;

public class Utils {

    static Main plugin = Main.getInstance();
    public static void Logger(String msg) {
        Bukkit.getLogger().info("[AxoCodes] " + msg);
    }
    public static String getMsg(String path) {
        String cfg = Main.getInstance().getConfig().getString("Messages." + path);
        String line = ChatColor.translateAlternateColorCodes('&', cfg);
        return line;
    }

    public static File customConfigFile;
    public static FileConfiguration storage;
    public static void createCustomConfig() {
        customConfigFile = new File(Main.getInstance().getDataFolder(), "storage.yml");
        try {
            if (!customConfigFile.exists()) {
                customConfigFile.createNewFile();
                storage = new YamlConfiguration();
                storage.load(customConfigFile);
            } else {
                storage = new YamlConfiguration();
                storage.load(customConfigFile);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void saveStorage() {
        customConfigFile = new File(Main.getInstance().getDataFolder(), "storage.yml");
        try {
            Main.getInstance().saveConfig();
            storage.save(customConfigFile);
            Utils.Logger("All data saved. Goodbye!");
        } catch (IOException e) {
            Bukkit.getLogger().warning("[AxoCodes] Could not save storage.yml");
        }
    }
    public static void reloadStorage() {
        try {
            storage.save(customConfigFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[AxoCodes] Could not save storage.yml");
        }
    }

    public static boolean isWalked(CommandSender sender) {
        int walked = Integer.parseInt(PlaceholderAPI.setPlaceholders(((Player) sender).getPlayer(), "%statistic_walk_one_cm%"));
        if ((walked / 100) >= Main.getInstance().getConfig().getInt("Settings.Walk")) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isPlayed(CommandSender sender) {
        int played = Integer.parseInt(PlaceholderAPI.setPlaceholders(((Player) sender).getPlayer(), "%statistic_seconds_played%"));
        if (played >= Main.getInstance().getConfig().getInt("Settings.Timed")) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isChat(CommandSender sender) {
        String saver = sender.getName();
        try {
            if (Utils.storage.getConfigurationSection("Players").getKeys(false).contains(saver)) {
                if (Utils.storage.getBoolean("Players." + saver + ".chat")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void Cmd(String cmd, CommandSender sender) {
        String neww = sender.getName();
        getServer().getScheduler().callSyncMethod(plugin, () -> {
            dispatchCommand(getConsoleSender(), cmd.replace("/", "").replace("{player}", neww));
            return null;
        });
    }
    public static void RunCmd(CommandSender sender, String cmd) {
        for (int i = 0; i < Main.getInstance().getConfig().getList("codes." + cmd + ".commands").size(); i++) {
            Cmd(Main.getInstance().getConfig().getList("codes." + cmd + ".commands").get(i).toString(), sender);
        }
    }
    public static boolean IsUsed(String code, CommandSender sender) {
        String rev = sender.getName();
        try {
            if (Utils.storage.getConfigurationSection("Players." + rev + ".codes").getKeys(false).contains(code)) {
                return false;
            } else {
                return true;
            }
        } catch (NullPointerException e) {
            return true;
        }
    }
    public static void Runner (CommandSender sender, String cmd) {
        if (!Utils.isWalked(sender)) {
            sender.sendMessage(Utils.getMsg("no-walk"));
        } else {
            if (!Utils.isPlayed(sender)) {
                sender.sendMessage(Utils.getMsg("no-time"));
            } else {
                if (!Utils.isChat(sender)) {
                    sender.sendMessage(Utils.getMsg("no-chat"));
                } else {
                    Utils.RunCmd(sender, cmd);
                    sender.sendMessage(Utils.getMsg("use"));
                    Utils.storage.createSection("Players." + sender.getName() + ".codes." + cmd);
                    Utils.reloadStorage();
                    try {
                        if (Utils.storage.getConfigurationSection("codes").getKeys(false).contains(cmd)) {
                            int old = Utils.storage.getInt("codes." + cmd + ".count");
                            Utils.storage.set("codes." + cmd + ".count", old + 1);
                            Utils.reloadStorage();
                        } else {
                            Utils.storage.createSection("codes." + cmd + ".count");
                            Utils.storage.set("codes." + cmd + ".count", 1);
                            Utils.reloadStorage();
                        }
                    } catch (NullPointerException exception) {
                        Utils.storage.createSection("codes." + cmd + ".count");
                        Utils.storage.set("codes." + cmd + ".count", 1);
                        Utils.reloadStorage();
                    }
                }
            }
        }
    }
}
