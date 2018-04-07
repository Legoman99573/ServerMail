package com.tyrellplayz.servermail.events;

import com.tyrellplayz.servermail.ServerMail;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener{

    private ServerMail sm;

    public PlayerEvents(ServerMail sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(sm.mailSort.containsKey(event.getPlayer())){
            sm.mailSort.remove(event.getPlayer());
        }
    }

}
