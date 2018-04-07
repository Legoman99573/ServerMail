package com.tyrellplayz.servermail.events;

import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.menus.EnumSort;
import com.tyrellplayz.servermail.menus.MailMenu;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEditBookEvent;

public class ChatEvents implements Listener{

    private ServerMail sm;

    public ChatEvents(ServerMail sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPayerChat(AsyncPlayerChatEvent event){
        if(sm.sendMessageMap.containsKey(event.getPlayer())){
            String message = event.getMessage();
            if(message.equalsIgnoreCase("cancel")){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+"Canceled");
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(event.getPlayer(),1, sm.mailSort.get(event.getPlayer()));
                return;
            }
            Mail mail = new Mail(event.getPlayer(), sm);
            mail.sendMail(sm.sendMessageMap.get(event.getPlayer()), ChatColor.translateAlternateColorCodes('&',message));
            event.setCancelled(true);
            MailMenu mailMenu = new MailMenu(sm);
            mailMenu.openInventory(event.getPlayer(),1, sm.mailSort.get(event.getPlayer()));
        }

    }

}
