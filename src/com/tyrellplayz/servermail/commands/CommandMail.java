package com.tyrellplayz.servermail.commands;

import com.tyrellplayz.servermail.MailUtil;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
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
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getPermissionMessage());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getMustBePlayerText());
                return true;
            }
            Player player = (Player)sender;
            MailMenu mailInventory = new MailMenu(sm);
            mailInventory.openInventory(player, 1, sm.mailSort.getOrDefault(player, EnumSort.LATEST));
            return true;
        }else if(command.getName().equalsIgnoreCase("maildisable")){
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+command.getPermissionMessage());
                return true;
            }
            if(sender instanceof Player){
                if(sm.playerMailMap.get(((Player) sender).getUniqueId()).isMailDisabled()){
                    sm.playerMailMap.get(((Player) sender).getUniqueId()).setMailDisabled(true);
                    sender.sendMessage(ChatColor.GREEN+LanguageConfig.getDisableMailText());
                }else{
                    sm.playerMailMap.get(((Player) sender).getUniqueId()).setMailDisabled(false);
                    sender.sendMessage(ChatColor.YELLOW+LanguageConfig.getEnableMailText());
                }
            }else{
                sender.sendMessage(ChatColor.RED+LanguageConfig.getMustBePlayerText());
            }
            return true;
        }
        return false;
    }
}
