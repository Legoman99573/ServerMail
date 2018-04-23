package com.tyrellplayz.servermail.events;

import com.tyrellplayz.servermail.MailUtil;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.menus.MailMenu;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatEvents implements Listener{

    private ServerMail sm;

    public ChatEvents(ServerMail sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPayerChat(AsyncPlayerChatEvent event){
        if(sm.sendMessageMap.containsKey(event.getPlayer())){
            String message = event.getMessage();
            if(message.equalsIgnoreCase(LanguageConfig.getCancelText())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getCanceledText());
                sm.sendMessageMap.remove(event.getPlayer());
                if(sm.sendMoneyAmountMap.containsKey(event.getPlayer())){
                    sm.sendMoneyAmountMap.remove(event.getPlayer());
                }
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(event.getPlayer(),1, sm.mailSort.get(event.getPlayer()));
                return;
            }
            if(sm.sendMoneyAmountMap.containsKey(event.getPlayer())){
                MailUtil mail = new MailUtil(event.getPlayer(), sm);
                mail.sendMail(sm.sendMessageMap.get(event.getPlayer()), ChatColor.translateAlternateColorCodes('&', message), sm.sendMoneyAmountMap.get(event.getPlayer()));
                event.setCancelled(true);
                sm.sendMessageMap.remove(event.getPlayer());
                ServerMail.getEcon().withdrawPlayer(event.getPlayer(),sm.sendMoneyAmountMap.get(event.getPlayer()));
                sm.sendMoneyAmountMap.remove(event.getPlayer());
                return;
            }
            MailUtil mail = new MailUtil(event.getPlayer(), sm);
            mail.sendMail(sm.sendMessageMap.get(event.getPlayer()), ChatColor.translateAlternateColorCodes('&',message));
            event.setCancelled(true);
            sm.sendMessageMap.remove(event.getPlayer());
        }else

        if(sm.sendItemMap.containsKey(event.getPlayer())){
            String message = event.getMessage();
            if(message.equalsIgnoreCase(LanguageConfig.getCancelText())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getCanceledText());
                sm.sendItemMap.remove(event.getPlayer());
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(event.getPlayer(),1, sm.mailSort.get(event.getPlayer()));
                return;
            }
            ItemStack itemStack = event.getPlayer().getItemInHand();
            if(itemStack == null){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getTypeItemText()+" "+LanguageConfig.getCancelText());
                return;
            }
            event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
            MailUtil mail = new MailUtil(event.getPlayer(), sm);
            mail.sendMail(sm.sendItemMap.get(event.getPlayer()), ChatColor.translateAlternateColorCodes('&',message), itemStack);
            event.setCancelled(true);
            sm.sendItemMap.remove(event.getPlayer());
        }else

        if(sm.sendMoneyMap.containsKey(event.getPlayer())){
            String message = event.getMessage();
            if(message.equalsIgnoreCase(LanguageConfig.getCancelText())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getCanceledText());
                sm.sendMoneyMap.remove(event.getPlayer());
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(event.getPlayer(),1, sm.mailSort.get(event.getPlayer()));
                return;
            }

            try{
                event.setCancelled(true);
                if(ServerMail.getEcon().getBalance(event.getPlayer()) < Double.parseDouble(message)){
                    event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getNotEnoughMoney());
                    event.getPlayer().sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMoneyText()+" "+LanguageConfig.getCancelText());
                    return;
                }
                if(Double.parseDouble(message) == 0){
                    event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getCantSendNothingText());
                    event.getPlayer().sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMoneyText()+" "+LanguageConfig.getCancelText());
                    return;
                }
                sm.sendMoneyAmountMap.put(event.getPlayer(), Double.parseDouble(message));
                event.getPlayer().sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMoneyMessageText()+" "+LanguageConfig.getCancelText());
                sm.sendMessageMap.put(event.getPlayer(), sm.sendMoneyMap.get(event.getPlayer()));
                sm.sendMoneyMap.remove(event.getPlayer());
            }catch (NumberFormatException ex){
                event.getPlayer().sendMessage(ChatColor.RED+LanguageConfig.getMustBeNumberText());
                event.getPlayer().sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMoneyText()+" "+LanguageConfig.getCancelText());
                event.setCancelled(true);
            }

        }

    }

}
