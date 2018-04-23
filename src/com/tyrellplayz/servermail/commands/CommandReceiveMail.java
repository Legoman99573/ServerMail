package com.tyrellplayz.servermail.commands;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.menus.EnumSort;
import com.tyrellplayz.servermail.menus.MailMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReceiveMail implements CommandExecutor{

    private ServerMail sm;
    public CommandReceiveMail(ServerMail sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+ LanguageConfig.getPermissionMessage());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED+ LanguageConfig.getMustBePlayerText());
                return true;
            }
            Player player = (Player)sender;
            int index = Integer.parseInt(args[0]);

            if(sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).hasItemStack() && !sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).isItemReceived()){
                sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).transferItem(player);
                sm.playerMailMap.get(player.getUniqueId()).save();
                sender.sendMessage(ChatColor.GREEN+LanguageConfig.getItemReceivedText());
            }else if(sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).hasMoney() && !sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).isMoneyReceived()){
                sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).transferMoney(player);
                sm.playerMailMap.get(player.getUniqueId()).save();
                sender.sendMessage(ChatColor.GREEN+"$"+sm.playerMailMap.get(player.getUniqueId()).getMailList().get(index).getMoney()+" "+LanguageConfig.getMoneyReceivedText());
            }else{
                sender.sendMessage(ChatColor.RED+LanguageConfig.getNothingToReceive());
            }
            return true;
        }
        return false;
    }
}
