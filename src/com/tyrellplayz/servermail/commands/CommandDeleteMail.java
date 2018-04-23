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

public class CommandDeleteMail implements CommandExecutor{

    private ServerMail sm;
    public CommandDeleteMail(ServerMail sm) {
        this.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            if(!sender.hasPermission(command.getPermission())){
                sender.sendMessage(ChatColor.RED+LanguageConfig.getPermissionMessage());
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED+ LanguageConfig.getMustBePlayerText());
                return true;
            }
            Player player = (Player)sender;

            int index = Integer.parseInt(args[0]);
            sm.playerMailMap.get(player.getUniqueId()).removeMail(index);
            sender.sendMessage(ChatColor.GREEN+LanguageConfig.getMessageDeletedText());

            MailMenu mailInventory = new MailMenu(sm);
            mailInventory.openInventory(player, 1, EnumSort.LATEST);
            return true;
        }
        return false;
    }
}
