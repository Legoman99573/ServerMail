package com.tyrellplayz.servermail.commands;

import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.menus.EnumSort;
import com.tyrellplayz.servermail.menus.MailMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMail implements CommandExecutor{

    private ServerMail sm;
    public CommandMail(ServerMail sm) { this.sm = sm; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("mail")){
            if(strings.length==1){
                Mail mail = new Mail(sm);
                mail.sendMailDebug(Bukkit.getOfflinePlayer("TyrellPlayz"), "Hello there, this is a message for you to enjoy");
            }
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+command.getPermissionMessage());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED+"You must be a player to view mail");
                return true;
            }
            Player player = (Player)sender;
            sender.sendMessage(ChatColor.GREEN+"Opening mail...");
            MailMenu mailInventory = new MailMenu(sm);
            mailInventory.openInventory(player, 1, sm.mailSort.getOrDefault(player, EnumSort.LATEST));
            return true;
        }else if(command.getName().equalsIgnoreCase("maildisable")){
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+command.getPermissionMessage());
                return true;
            }
            if(sender instanceof Player){
                if(sm.disableMail((Player)sender)){
                    sender.sendMessage(ChatColor.GREEN+"You will no longer receive mail from other players");
                }else{
                    sender.sendMessage(ChatColor.YELLOW+"You can now receive mail from other players");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You must be a player to disable mail");
            }
            return true;
        }
        return false;
    }
}
