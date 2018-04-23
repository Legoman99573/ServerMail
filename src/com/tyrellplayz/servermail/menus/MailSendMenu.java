package com.tyrellplayz.servermail.menus;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.configs.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MailSendMenu extends CustomInventory implements Listener{

    public static String title = "Choose mail type";
    private int rows = 1;

    private static Inventory inv;
    private ServerMail sm;

    public MailSendMenu(ServerMail sm) {
        this.sm = sm;
    }

    public void openInventory(Player player, OfflinePlayer receiver){
        inv = Bukkit.createInventory(null,getSize(rows),title);

        if(player.hasPermission("servermail.mail.message")){
            createDisplay(inv, Material.BOOK_AND_QUILL, 0, ChatColor.GOLD+LanguageConfig.getSendMessageMessage(), receiver.getName());
        }
        if(MainConfig.getItemPackages() && player.hasPermission("servermail.mail.items")){
            createDisplay(inv, Material.CHEST, 1, ChatColor.GOLD+LanguageConfig.getSendItemMessage(), receiver.getName());
        }
        if(MainConfig.getMoneyPackages() && player.hasPermission("servermail.mail.money")){
            createDisplay(inv, Material.GOLD_INGOT, 2, ChatColor.GOLD+LanguageConfig.getSendMoneyMessage(), receiver.getName());
        }

        createDisplay(inv, Material.BARRIER, 8, ChatColor.RED+ LanguageConfig.getBackText());

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if(inventory.getName().contains(title)){
            // Stops a player from taking items out
            event.setCancelled(true);
            // Checks if anything as clicked
            if(clicked == null)return;

            if(clicked.getType()==Material.BOOK_AND_QUILL){
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(clicked.getItemMeta().getLore().get(0));
                sm.sendMessageMap.put(player, receiver);
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMessageText()+" "+LanguageConfig.getCancelText());
            }

            if(clicked.getType()==Material.CHEST){
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(clicked.getItemMeta().getLore().get(0));
                sm.sendItemMap.put(player, receiver);
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN+LanguageConfig.getTypeItemText()+" "+LanguageConfig.getCancelText());
            }

            if(clicked.getType()==Material.GOLD_INGOT){
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(clicked.getItemMeta().getLore().get(0));
                sm.sendMoneyMap.put(player, receiver);
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN+LanguageConfig.getTypeMoneyText()+" "+LanguageConfig.getCancelText());
            }

            if(clicked.getType()==Material.BARRIER){
                PlayerMenu playerMenu = new PlayerMenu(sm);
                playerMenu.openInventory(player,1);
            }

        }

    }

}
