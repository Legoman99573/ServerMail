package com.tyrellplayz.servermail;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mail implements IMail{

    private Player sender;
    private ServerMail sm;

    /**
     * Send mail to a player
     * @param sender - Player sending the mail
     * @param sm - ServerMail
     */
    public Mail(Player sender, ServerMail sm) {
        this.sender = sender;
        this.sm = sm;
    }

    @Override
    public boolean sendMail(Player receiver, String message) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+"You cant send nothing");
            return false;
        }
        if(sm.hasMailDisabled(receiver.getUniqueId())) {
            sender.sendMessage(ChatColor.RED+"Player has disabled mail");
            return false;
        }
        if(sm.messagesMap.containsKey(receiver.getUniqueId())){
            sm.messagesMap.get(receiver.getUniqueId()).addMessage(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
        }else{
            List<String> messageList = new ArrayList<>();
            messageList.add(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
            sm.messagesMap.put(receiver.getUniqueId(), new Messages(receiver.getUniqueId(), messageList));
        }
        saveMail();
        return true;
    }

    @Override
    public boolean sendMail(OfflinePlayer receiver, String message) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+"You cant send nothing");
            return false;
        }
        if(sm.hasMailDisabled(receiver.getUniqueId())) {
            sender.sendMessage(ChatColor.RED+"Player has disabled mail");
            return false;
        }
        if(sm.messagesMap.containsKey(receiver.getUniqueId())){
            sm.messagesMap.get(receiver.getUniqueId()).addMessage(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
        }else{
            List<String> messageList = new ArrayList<>();
            messageList.add(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
            sm.messagesMap.put(receiver.getUniqueId(), new Messages(receiver.getUniqueId(), messageList));
        }
        saveMail();
        return true;
    }

    @Override
    public boolean sendMail(UUID receiver, String message) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase("")) {
            sender.sendMessage(ChatColor.RED+"You cant send nothing");
            return false;
        }
        if(sm.hasMailDisabled(receiver)) {
            sender.sendMessage(ChatColor.RED+"Player has disabled mail");
            return false;
        }
        if(sm.messagesMap.containsKey(receiver)){
            sm.messagesMap.get(receiver).addMessage(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
        }else{
            List<String> messageList = new ArrayList<>();
            messageList.add(sender.getName()+"/"+System.currentTimeMillis()+"/"+message);
            sm.messagesMap.put(receiver, new Messages(receiver, messageList));
        }
        saveMail();
        return true;
    }

    public Mail(ServerMail sm) {
        this.sm = sm;
    }

    public boolean sendMailDebug(OfflinePlayer receiver, String message) {
        if(receiver==null)return false;
        if(message.equalsIgnoreCase(""))return false;
        if(sm.hasMailDisabled(receiver.getUniqueId()))return false;
        if(sm.messagesMap.containsKey(receiver.getUniqueId())){
            sm.messagesMap.get(receiver.getUniqueId()).addMessage("Debug/"+System.currentTimeMillis()+"/"+message);
        }else{
            List<String> messageList = new ArrayList<>();
            messageList.add("Debug/"+System.currentTimeMillis()+"/"+message);
            sm.messagesMap.put(receiver.getUniqueId(), new Messages(receiver.getUniqueId(), messageList));
        }
        saveMail();
        return true;
    }

    private void saveMail(){
        sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.saveMessages());
    }

}
