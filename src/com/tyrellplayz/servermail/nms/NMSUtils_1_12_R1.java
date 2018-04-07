package com.tyrellplayz.servermail.nms;

import com.tyrellplayz.servermail.ServerMail;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class NMSUtils_1_12_R1 implements NMSUtils{

    @Override
    public void createBookDisplayForMail(ServerMail sm, Player player, Inventory inventory, int slot, String title, String sender, String message) {
        BookMeta bookMeta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        bookMeta.setTitle(title);
        bookMeta.setAuthor(sender);
        int messageIndex = 0;
        for(String message1:sm.messagesMap.get(player.getUniqueId()).getMessages()){
            if(message1.contains(message)){
                messageIndex = sm.messagesMap.get(player.getUniqueId()).getMessages().indexOf(message1);
            }
        }
        List<IChatBaseComponent> pages;
        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
        }catch (ReflectiveOperationException ex){
            ex.printStackTrace();
            return;
        }

        String jsonText = "[{\"text\":\""+message+" \\n\"},{\"text\":\"Sent by: "+sender+" \\n\",\"color\":\"gold\"},{\"text\":\"[Back]\\n\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mail\"}},{\"text\":\"[Delete]\\n\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/deletemail "+messageIndex+"\"}},{\"text\":\"[Reply]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"sendmail "+sender+"\"}}]";
        IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a(jsonText);

        pages.add(text);
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        item.setItemMeta(bookMeta);
        inventory.setItem(slot, item);
    }
}
