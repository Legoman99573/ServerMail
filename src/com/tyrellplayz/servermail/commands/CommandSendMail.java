package com.tyrellplayz.servermail.commands;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSendMail implements CommandExecutor{

    private ServerMail sm;

    public CommandSendMail(ServerMail sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(command.getPermission())){
            commandSender.sendMessage(ChatColor.RED+LanguageConfig.getPermissionMessage());
            return true;
        }
        if(strings.length==1){
            if(commandSender instanceof Player){
                Player player = (Player)commandSender;
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(strings[0]);
                sm.sendMessageMap.put(player, receiver);
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN+ LanguageConfig.getTypeMessageText()+" "+LanguageConfig.getCancelText());
                return true;
            }else{
                commandSender.sendMessage(ChatColor.RED+LanguageConfig.getMustBePlayerText());
                return true;
            }
        }
        return true;
    }
}
