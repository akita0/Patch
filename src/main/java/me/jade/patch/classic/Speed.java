package me.jade.patch.classic;/*

@Author https://github.com/akita0
2023

*/

import me.jade.Patcher;
import me.jade.patch.Patch;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class Speed extends Patch {
    public Speed() {
        super("Speed");
    }

    public final HashMap<String,Double> map = new HashMap<String,Double>();
    @Override
    public void handleEvent(Event event) {
        if(!Patcher.config.SpeedPatch)
            return;
        if(event instanceof PlayerMoveEvent) {
            PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
            if(moveEvent.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
            if(moveEvent.getFrom().getY() != moveEvent.getTo().getY()) {
                double diff = Math.abs(moveEvent.getFrom().getY() - moveEvent.getTo().getY());
                double xDiff = Math.abs(moveEvent.getFrom().getX() - moveEvent.getTo().getX());
                double zDiff = Math.abs(moveEvent.getFrom().getZ() - moveEvent.getTo().getZ());
                if (moveEvent.getPlayer().getFallDistance() < 1) {
                    if (diff < 0.0626 || (diff > 0.5 && diff < 0.5626)) {
                        if (xDiff > 0.614242424242 || zDiff > 0.61424242424242) {
                            String speed = diff < 0.03 ? "Ground" : "HighGround";
                            log(moveEvent.getPlayer().getName() + " failed Speed [Data:" + speed + ":" + xDiff + ":" + zDiff + ":" + diff + "]");
                            moveEvent.setTo(moveEvent.getFrom());
                        }
                    }
                    if(diff > 0.12 && diff < 0.5 - 1E-13) {
                        if (xDiff > 0.754242424242 || zDiff > 0.75424242424242) {
                            if (!map.containsKey(moveEvent.getPlayer().getName())) {
                                map.put(moveEvent.getPlayer().getName(), diff);
                                return;
                            }
                            if (diff == map.get(moveEvent.getPlayer().getName())) {
                                map.put(moveEvent.getPlayer().getName(), diff);
                                log(moveEvent.getPlayer().getName() + " failed Speed [Data:LowHop:" + xDiff + ":" + zDiff + ":" + diff + "]");
                                moveEvent.setTo(moveEvent.getFrom());
                            }
                        }
                    }if(diff > 0.51) {
                        if (xDiff > 0.614242424242 || zDiff > 0.61424242424242) {
                             log(moveEvent.getPlayer().getName() + " failed Speed [Data:Hop:" + xDiff + ":" + zDiff + ":" + diff + "]");
                             moveEvent.setTo(moveEvent.getFrom());
                        }
                    }
                }
            }
        }
        super.handleEvent(event);
    }

}
