package me.jade.patch.classic;/*

@Author https://github.com/akita0
2023

*/

import me.jade.Patcher;
import me.jade.patch.Patch;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;


public class AutoDrink extends Patch {
    public AutoDrink(){
        super("AutoHeal");
    }
    private final HashMap<String,Long> delayedSwitch = new HashMap<>();
    private final HashMap<String,Long> lastSwing = new HashMap<>();
    private final HashMap<String,Long> times = new HashMap<>();
    private final HashMap<String,Long> drinks = new HashMap<>();
    public final ArrayList<String> blackList = new ArrayList<String>();
    private final ArrayList<String> punishAtNextHeal = new ArrayList<>();
    @Override
    public void handleEvent(Event event) {
        if(!Patcher.config.DrinkPatch)
            return;
        if (event instanceof InventoryClickEvent) {
            InventoryClickEvent dragEvent = (InventoryClickEvent) event;
            if (dragEvent.getClickedInventory() != null && dragEvent.getClickedInventory().equals(dragEvent.getWhoClicked().getInventory())) {
                ItemStack clickedItem = dragEvent.getCurrentItem();
                if (clickedItem != null && (clickedItem.getType() == Material.POTION || clickedItem.getType() == Material.DIAMOND_SWORD)) {
                    if(times.get(dragEvent.getWhoClicked().getName()) != null && System.currentTimeMillis() < times.get(dragEvent.getWhoClicked().getName()) + 3000) {
                        log(dragEvent.getWhoClicked().getName() + " failed AutoDrink [Data:SWAP:TYPE-A:" + ((System.currentTimeMillis() - times.get(dragEvent.getWhoClicked().getName()))) + "]");
                        dragEvent.setCancelled(true);
                    }
                }
            }
         }
        if(event instanceof PlayerAnimationEvent) {
            if(((PlayerAnimationEvent) event).getAnimationType() == PlayerAnimationType.ARM_SWING) {
                lastSwing.remove(((PlayerAnimationEvent) event).getPlayer().getName());
                lastSwing.put(((PlayerAnimationEvent) event).getPlayer().getName() , System.currentTimeMillis());
            }
        }
        if(event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if(entityEvent.getEntity() instanceof HumanEntity) {
                HumanEntity entity = (HumanEntity) entityEvent.getEntity();
                if(times.get(entity.getName()) != null) {
                    times.remove(entity.getName());
                }
                times.put(entity.getName() , System.currentTimeMillis());
            }
        }
        if (event instanceof EntityRegainHealthEvent) {
            if (((EntityRegainHealthEvent) event).getEntity() instanceof Player && ((EntityRegainHealthEvent) event).getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC) {
                drinks.remove(((EntityRegainHealthEvent) event).getEntity().getName());
                drinks.put(((EntityRegainHealthEvent) event).getEntity().getName(), System.currentTimeMillis());
                if (punishAtNextHeal.contains(((EntityRegainHealthEvent) event).getEntity().getName())) {
                    ((EntityRegainHealthEvent) event).setCancelled(true);
                    punishAtNextHeal.remove(((EntityRegainHealthEvent) event).getEntity().getName());
                }
            }
        }
        if (event instanceof PlayerItemHeldEvent) {
            if(isType(Material.POTION , (PlayerItemHeldEvent) event)) {
                long gotPotion = System.currentTimeMillis();
                Patcher.plugin.getServer().getScheduler().runTaskLater(Patcher.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if(!lastSwing.containsKey(((PlayerItemHeldEvent) event).getPlayer().getName()))
                            return;
                        long time = lastSwing.get(((PlayerItemHeldEvent) event).getPlayer().getName()) - gotPotion;
                        if(time < 55) {
                            log(((PlayerItemHeldEvent) event).getPlayer().getName() + " failed AutoDrink [Data:SWITCH:TYPE-C:" + time + "]");
                            if (!punishAtNextHeal.contains(((PlayerItemHeldEvent) event).getPlayer().getName()))
                                punishAtNextHeal.add(((PlayerItemHeldEvent) event).getPlayer().getName());
                        }
                        }
                } , 5);
            }
            if (drinks.containsKey(((PlayerItemHeldEvent) event).getPlayer().getName())) {
                long time = System.currentTimeMillis() - drinks.get(((PlayerItemHeldEvent) event).getPlayer().getName());
                if (time < 10) {
                    drinks.put(((PlayerItemHeldEvent) event).getPlayer().getName(), System.currentTimeMillis());
                    log(((PlayerItemHeldEvent) event).getPlayer().getName() + " failed AutoDrink [Data:SWITCH:TYPE-B:" + time + "]");
                    if (!punishAtNextHeal.contains(((PlayerItemHeldEvent) event).getPlayer().getName()))
                        punishAtNextHeal.add(((PlayerItemHeldEvent) event).getPlayer().getName());
                }
            }
        }
        if(event instanceof PlayerDeathEvent) {
            blackList.remove(((PlayerDeathEvent) event).getEntity().getName());
        }
    }
    public boolean isType(Material m , PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        return (item != null && item.getType() == m);
    }
}
