package ru.axiefeat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void AsyncChatEvent(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String saver = player.getName();

        String chat = Main.getInstance().getConfig().getString("Settings.Chat");
        try {
            if (!(Utils.storage.getBoolean("Players." + saver + ".chat"))) {
                if (event.getMessage().equalsIgnoreCase(chat)) {
                    Utils.storage.createSection("Players." + saver + ".chat");
                    Utils.storage.createSection("Players." + saver + ".codes");
                    Utils.storage.set("Players." + saver + ".chat", true);
                    Utils.reloadStorage();
                }
            }
        } catch (NullPointerException e) {
            if (event.getMessage().equalsIgnoreCase(chat)) {
                Utils.storage.createSection("Players." + saver + ".chat");
                Utils.storage.createSection("Players." + saver + ".codes");
                Utils.storage.set("Players." + saver + ".chat", true);
                Utils.reloadStorage();
            }
        }
    }
}
