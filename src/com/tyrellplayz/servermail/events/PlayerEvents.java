package com.tyrellplayz.servermail.events;

import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.Updater;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.configs.MainConfig;
import com.tyrellplayz.servermail.nms.NMSUtils_1_12_R1;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import com.tyrellplayz.servermail.utils.ToastMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Comparator;

/**
 * Handles all player events
 */
public class PlayerEvents implements Listener{

    private ServerMail sm;

    public PlayerEvents(ServerMail sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        // Checks to see if player has a data file loaded
        if(!sm.playerMailMap.contains(player.getUniqueId())){
            // Runs a task to create a new player data file for the player
            sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.playerMailMap.addPlayerMail(player.getUniqueId()));
        }
        // Checks if the name in the players data file is the same as the players name
        try
        if(!sm.playerMailMap.get(player.getUniqueId()).getPlayerName().equals(player.getName())){
            // Changes the players name in config to the updated one
            sm.playerMailMap.get(player.getUniqueId()).setPlayerName(player.getName());
        }
        // Runs a task to check if the player has any unread messages
        sm.getServer().getScheduler().runTaskLaterAsynchronously(sm, () -> newMessagesTask(player),80);

        if(!ServerMail.offlinePlayers.contains(event.getPlayer())){
            ServerMail.offlinePlayers.add(event.getPlayer());
            sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> ServerMail.offlinePlayers.sort(Comparator.comparing(playerOne -> (playerOne).getName())));
        }

        // Checks if player has the permission to receive update messages
        if(player.hasPermission("servermail.updates") && MainConfig.getUpdateMessageOnJoin()){
            if(MainConfig.getUpdateChecker()){
                sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> updateMessageOnJoin(player));
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        // Remove player from the mailSort list if they are in it
        if(sm.mailSort.containsKey(event.getPlayer())){
            sm.mailSort.remove(event.getPlayer());
        }
    }

    /**
     * Sends a update message to given {@link Player} if there is an update
     * @param player
     */
    private void updateMessageOnJoin(Player player){
        Updater updater = new Updater(sm, 55429);
        try {
            if (updater.checkForUpdates()){
                player.sendMessage(ChatColor.GREEN+"New update available for ServerMail");
                player.sendMessage(ChatColor.GOLD+"This version: "+ChatColor.RESET+ServerMail.pdf.getVersion());
                player.sendMessage(ChatColor.GOLD+"New version: "+ChatColor.RESET+updater.getLatestVersion());
                player.sendMessage(ChatColor.GREEN+ "Download here: "+ChatColor.RESET+updater.getResourceURL());
            }
        } catch (Exception e) {
            player.sendMessage(ChatColor.YELLOW+"Could not check for updates!");
        }
    }

    /**
     * Sends a notification to given {@link Player} if they have unreead messages
     * @param player
     */
    private void newMessagesTask(Player player){
        for(Mail mail:sm.playerMailMap.get(player.getUniqueId()).getMailList()){
            if(!mail.isRead()){
                // Has unread messages
                if(MainConfig.getToastNotifications()){
                    sm.toastMessage.showTo(player);
                }else{
                    player.sendMessage(ChatColor.GREEN+ LanguageConfig.getNewMailText());
                }
                return;
            }
        }
    }

}
