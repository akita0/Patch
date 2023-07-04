package me.jade.patch;/*

@Author https://github.com/akita0
2023

*/

import me.jade.Patcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.logging.Logger;

public class Patch {
    private String name;

    public void handleEvent(Event event) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Patch(String name) {
        this.name = name;
    }
    public void log(String s) {
        String message = Patcher.config.prefix +" | " + this.name + ": " + s;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(PatchManager.notify.contains(player.getName()))
                player.sendMessage(message);
        }
        Logger.getAnonymousLogger().info(message);
    }
}
