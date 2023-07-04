package me.jade.event;/*

@Author https://github.com/akita0
2023

*/

import me.jade.patch.PatchManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GlobalListener implements Listener {
    @EventHandler
    public void event(InventoryClickEvent event) {
        handle(event);
    }
    @EventHandler
    public void event2(EntityDamageByEntityEvent event) {
        handle(event);
    }
    @EventHandler
    public void event3(PlayerMoveEvent event) {
        handle(event);
    }
    @EventHandler
    public void event4(EntityRegainHealthEvent event) {
        handle(event);
    }
    @EventHandler
    public void event5(PlayerItemHeldEvent event) {
        handle(event);
    }
    @EventHandler
    public void event6(PlayerDeathEvent event) {
        handle(event);
    }
    @EventHandler
    public void event7(PlayerAnimationEvent event) {
        handle(event);
    }
    public void handle(Event e) {
        PatchManager.patches.forEach(p -> {
            p.handleEvent(e);
        });
    }

}
