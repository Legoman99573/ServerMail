package com.tyrellplayz.servermail.nms;

import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class NMSUtils_1_9_R1 implements NMSUtils{

    @Override
    public void createBookDisplayForMail(ServerMail sm, Player player, Inventory inventory, int slot, String title, String sender, String message) {
        BookMeta bookMeta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        bookMeta.setTitle(title);
        bookMeta.setAuthor(sender);
        int mailIndex = 0;
        Mail mail;
        for(Mail m:sm.playerMailMap.get(player.getUniqueId()).getMailList()){
            if(m.getMessage().contains(message)){
                mailIndex = sm.playerMailMap.get(player.getUniqueId()).getMailList().indexOf(m);
            }
        }
        mail = sm.playerMailMap.get(player.getUniqueId()).getMailList().get(mailIndex);
        List<IChatBaseComponent> pages;
        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
        }catch (ReflectiveOperationException ex){
            ex.printStackTrace();
            return;
        }

        String jsonText;
        if(mail.hasItemStack() && !mail.isItemReceived()){
            jsonText = "[{\"text\":\""+message+"\\n\\n\"},{\"text\":\""+LanguageConfig.getSentByText()+" "+sender+"\\n\",\"color\":\"gold\",\"bold\":true,\"underlined\":false},{\"text\":\"["+LanguageConfig.getReceiveText()+"]\\n\",\"color\":\"dark_purple\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/receivemail "+mailIndex+"\"}},{\"text\":\"["+LanguageConfig.getReplyText()+"]\\n\",\"color\":\"blue\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/sendmail "+sender+"\"}},{\"text\":\"["+LanguageConfig.getDeleteText()+"]\\n\",\"color\":\"red\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/deletemail "+mailIndex+"\"}},{\"text\":\"["+LanguageConfig.getBackText()+"]\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mail\"}}]";
        }else if(mail.hasMoney() && !mail.isMoneyReceived()){
            jsonText = "[{\"text\":\""+message+"\\n\\n\"},{\"text\":\""+LanguageConfig.getSentByText()+" "+sender+"\\n\",\"color\":\"gold\",\"bold\":true,\"underlined\":false},{\"text\":\"["+LanguageConfig.getReceiveText()+"]\\n\",\"color\":\"dark_purple\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/receivemail "+mailIndex+"\"}},{\"text\":\"["+LanguageConfig.getReplyText()+"]\\n\",\"color\":\"blue\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/sendmail "+sender+"\"}},{\"text\":\"["+LanguageConfig.getDeleteText()+"]\\n\",\"color\":\"red\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/deletemail "+mailIndex+"\"}},{\"text\":\"["+LanguageConfig.getBackText()+"]\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mail\"}}]";
        }else{
            jsonText = "[{\"text\":\""+message+"\\n\\n\"},{\"text\":\""+LanguageConfig.getSentByText()+" "+sender+"\\n\",\"color\":\"gold\",\"bold\":true,\"underlined\":false},{\"text\":\"["+LanguageConfig.getReplyText()+"]\\n\",\"color\":\"blue\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/sendmail "+sender+"\"}},{\"text\":\"["+LanguageConfig.getDeleteText()+"]\\n\",\"color\":\"red\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/deletemail "+mailIndex+"\"}},{\"text\":\"["+LanguageConfig.getBackText()+"]\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mail\"}}]";
        }

        IChatBaseComponent text = IChatBaseComponent.ChatSerializer.a(jsonText);

        pages.add(text);
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        item.setItemMeta(bookMeta);
        inventory.setItem(slot, item);
    }
}
