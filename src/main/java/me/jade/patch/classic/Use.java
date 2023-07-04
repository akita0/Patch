package me.jade.patch.classic;/*

@Author https://github.com/akita0
2023

*/

import me.jade.Patcher;
import me.jade.patch.Patch;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.HashMap;

public class Use extends Patch {
    public Use() {
        super("Use");
    }
    private final HashMap<String,Long> map = new HashMap<>();
    @Override
    public void handleEvent(Event event) {
        if(!Patcher.config.UsePatch)
            return;
        if(event instanceof EntityRegainHealthEvent) {
            if(((EntityRegainHealthEvent) event).getEntity() instanceof Player && ((EntityRegainHealthEvent) event).getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC) {
                if(map.containsKey(((EntityRegainHealthEvent) event).getEntity().getName())) {
                    long time = System.currentTimeMillis() - map.get(((EntityRegainHealthEvent) event).getEntity().getName());
                    if(time < Patcher.config.UseSpeed) {
                        log(((EntityRegainHealthEvent) event).getEntity().getName() + " failed InsantUse [Data:" + time+"]");
                        ((EntityRegainHealthEvent) event).setCancelled(true);
                    }
                }
            }
        }
        if(event instanceof PlayerItemHeldEvent) {
            if(((PlayerItemHeldEvent) event).getPlayer().getInventory().getItem(((PlayerItemHeldEvent) event).getNewSlot()) != null && ((PlayerItemHeldEvent) event).getPlayer().getInventory().getItem(((PlayerItemHeldEvent) event).getNewSlot()).getType() == Material.POTION) {
                map.remove(((PlayerItemHeldEvent) event).getPlayer().getName());
                map.put(((PlayerItemHeldEvent) event).getPlayer().getName() , System.currentTimeMillis());
            }
        }
        super.handleEvent(event);
    }
}
