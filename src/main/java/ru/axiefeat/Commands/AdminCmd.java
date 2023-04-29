package ru.axiefeat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.axiefeat.Main;
import ru.axiefeat.Utils;

public class AdminCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 | args.length > 2) {
            sender.sendMessage("§cНеизвестный аргумент!");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equals("reload")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Utils.getMsg("only-player"));
                } else {
                    if (sender.hasPermission("fpcode.reload")) {
                        Main.getInstance().reloadConfig();
                        Utils.reloadStorage();
                        sender.sendMessage(Utils.getMsg("reload"));
                    } else {
                        sender.sendMessage(Utils.getMsg("perm"));
                    }
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equals("stat")) {
                if (sender.hasPermission("axocode.stat")) {
                    if (Main.getInstance().getConfig().getConfigurationSection("codes").getKeys(false).contains(args[1])) {
                        try {
                            String usages = Utils.storage.getString("codes." + args[1] + ".count");
                            sender.sendMessage(Utils.getMsg("stat").replace("{code}", args[1]).replace("{usages}", usages));
                        } catch (NullPointerException ignored) {
                            sender.sendMessage(Utils.getMsg("stat").replace("{code}", args[1]).replace("{usages}", Utils.getMsg("not")));
                        }
                    } else {
                        sender.sendMessage(Utils.getMsg("not-found").replace("{code}", args[1]));
                    }
                } else {
                    sender.sendMessage(Utils.getMsg("perm"));
                }
            }
        }
        return true;
    }
}
