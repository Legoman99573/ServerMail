package com.tyrellplayz.servermail;

import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MailUtil implements IMailUtil {

    private Player sender;
    private ServerMail sm;

    /**
     * Send mail to a player
     * @param sender - Player sending the mail
     * @param sm - ServerMail
     */
    public MailUtil(Player sender, ServerMail sm) {
        this.sender = sender;
        this.sm = sm;
    }

    @Override
    public boolean sendMail(OfflinePlayer receiver, String message) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getCantSendNothingText());
            return false;
        }
        if(sm.playerMailMap.contains(receiver.getUniqueId())){
            if(sm.playerMailMap.get(receiver.getUniqueId()).isMailDisabled()){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerDisabledMailText());
                return false;
            }
            sendMailTask(receiver.getUniqueId(), message);
            sender.sendMessage(ChatColor.GREEN+LanguageConfig.getMailSentText());
        }else {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerNeverOnline());
            return false;
        }
        return true;
    }

    @Override
    public boolean sendMail(OfflinePlayer receiver, String message, ItemStack itemStack) {
        //if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getCantSendNothingText());
            return false;
        }
        if(sm.playerMailMap.contains(receiver.getUniqueId())){
            if(sm.playerMailMap.get(receiver.getUniqueId()).isMailDisabled()){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerDisabledMailText());
                return false;
            }
            sendMailTask(receiver.getUniqueId(), message, itemStack);
            sender.sendMessage(ChatColor.GREEN+LanguageConfig.getMailSentText());
        }else {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerNeverOnline());
            return false;
        }
        return true;
    }

    @Override
    public boolean sendMail(OfflinePlayer receiver, String message, Double money) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getCantSendNothingText());
            return false;
        }
        if(sm.playerMailMap.contains(receiver.getUniqueId())){
            if(sm.playerMailMap.get(receiver.getUniqueId()).isMailDisabled()){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerDisabledMailText());
                return false;
            }
            sendMailTask(receiver.getUniqueId(), message, money);
            sender.sendMessage(ChatColor.GREEN+LanguageConfig.getMailSentText());
        }else {
            sender.sendMessage(ChatColor.RED+LanguageConfig.getPlayerNeverOnline());
            return false;
        }
        return true;
    }

    private void sendMailTask(final UUID receiver, final String message){
        sm.getServer().getScheduler().runTaskAsynchronously(sm, new Runnable() {
            @Override
            public void run() {
                String newMessage = Utils.removeBlockedWords(message);
                sm.playerMailMap.get(receiver).addMail(new Mail(sender.getName(), newMessage, false));
                sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.playerMailMap.get(receiver).save());
            }
        });
    }

    private void sendMailTask(final UUID receiver, final String message, final ItemStack itemStack){
        sm.getServer().getScheduler().runTaskAsynchronously(sm, new Runnable() {
            @Override
            public void run() {
                String newMessage = Utils.removeBlockedWords(message);
                sm.playerMailMap.get(receiver).addMail(new Mail(sender.getName(), newMessage, false, itemStack, false));
                sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.playerMailMap.get(receiver).save());
            }
        });
    }

    private void sendMailTask(final UUID receiver, final String message, final double money){
        sm.getServer().getScheduler().runTaskAsynchronously(sm, new Runnable() {
            @Override
            public void run() {
                String newMessage = Utils.removeBlockedWords(message);
                sm.playerMailMap.get(receiver).addMail(new Mail(sender.getName(), newMessage, false, money, false));
                sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.playerMailMap.get(receiver).save());
            }
        });
    }

}
