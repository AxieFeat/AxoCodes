package ru.axiefeat.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.axiefeat.Main;
import ru.axiefeat.Utils;

public class CodeCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 | args.length > 1) {
            sender.sendMessage(Utils.getMsg("code-usage"));
            return true;
        }
        if (args.length == 1) {
            long unix = System.currentTimeMillis() / 1000L;
            if (sender.hasPermission("axocode.code")) {
                if (Utils.IsUsed(args[0], sender)) {
                    if (Main.getInstance().getConfig().getConfigurationSection("codes").getKeys(false).contains(args[0])) {
                        if (unix <= Main.getInstance().getConfig().getInt("codes." + args[0] + ".date")) {
                            Utils.Runner(sender, args[0]);
                        } else {
                            if (Main.getInstance().getConfig().getInt("codes." + args[0] + ".date") == -1) {
                                Utils.Runner(sender, args[0]);
                            } else {
                                sender.sendMessage(Utils.getMsg("not-found").replace("{code}", args[0]));
                            }
                        }
                    } else {
                        sender.sendMessage(Utils.getMsg("not-found").replace("{code}", args[0]));
                    }
                } else {
                    sender.sendMessage(Utils.getMsg("already").replace("{code}", args[0]));
                }
            }
        } else {
            sender.sendMessage(Utils.getMsg("perm").replace("{code}", args[0]));
        }
        return true;
    }
}
