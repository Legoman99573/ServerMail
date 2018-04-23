package com.tyrellplayz.servermail.menus;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.configs.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PlayerMenu extends CustomInventory implements Listener{

    public static String title = "Pick player : Page ";
    private int rows = 6;

    private static Inventory inv;
    private ServerMail sm;

    public PlayerMenu(ServerMail sm) {
        this.sm = sm;
    }

    public void openInventory(Player player, int page){
        inv = Bukkit.createInventory(null,getSize(rows),title+page);
        inv.setMaxStackSize(1);

        /*
        Players
         */
        int first = 45*(page-1)+1;
        int last = 45*page;
        int i = 1;
        int slot = 0;
        List<OfflinePlayer> players = ServerMail.offlinePlayers;
        for(OfflinePlayer offlinePlayer:players){
            if((i>=first)&&(i<=last)){
                createHeadDisplay(inv, offlinePlayer,slot, LanguageConfig.getPlayerHeadLoreText());
                slot++;
            }
            i++;
        }
        /*
        The Task Bar
         */
        //createDisplay(inv, Material.SIGN, 45,ChatColor.GOLD+LanguageConfig.getNameText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 46, "", MainConfig.getPaneColour());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 47, "", MainConfig.getPaneColour());
        createDisplay(inv, Material.PAPER, 48,ChatColor.GOLD+LanguageConfig.getPreviousPageText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 49, "", MainConfig.getPaneColour());
        createDisplay(inv, Material.PAPER, 50, ChatColor.GOLD+LanguageConfig.getNextPageText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 51, "", MainConfig.getPaneColour());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 52, "", MainConfig.getPaneColour());
        createDisplay(inv, Material.BARRIER, 53, ChatColor.RED+LanguageConfig.getBackText());

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if(inventory.getName().contains(title)){
            int maxPages;
            maxPages = (int)Math.ceil(ServerMail.offlinePlayers.size()/45.0);
            // Stops a player from taking items out
            event.setCancelled(true);
            // Checks if anything as clicked
            if(clicked == null)return;
            // Handles clicked player
            if(clicked.getType() == Material.SKULL_ITEM || clicked.getType() == Material.SKULL){
                OfflinePlayer receiver = Bukkit.getServer().getOfflinePlayer(clicked.getItemMeta().getDisplayName());
                MailSendMenu mailSendMenu = new MailSendMenu(sm);
                mailSendMenu.openInventory(player,receiver);
            }
            // Goes back to mail menu
            if(clicked.getType() == Material.BARRIER){
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(player, 1, sm.mailSort.get(player));
            }
            // Handles entering a player name
            if(clicked.getType() == Material.SIGN){
                sm.signMenu.open(player, new String[]{LanguageConfig.getEnterNameText(), "==========", "", "=========="}, (player1, text) -> {
                    OfflinePlayer receiver = Bukkit.getServer().getOfflinePlayer(text[2]);
                    MailSendMenu mailSendMenu = new MailSendMenu(sm);
                    mailSendMenu.openInventory(player,receiver);
                });
            }
            if(clicked.getType() == Material.PAPER){
                // Handles going to a Previous Page
                if(clicked.getItemMeta().getDisplayName().contains(LanguageConfig.getPreviousPageText())){
                    String str = inventory.getName().replaceAll("\\D+","");
                    int pPage =  Integer.parseInt(str) - 1;
                    if(pPage < 1){
                        return;
                    }
                    // Open the menu again for the Previous Page
                    PlayerMenu playerMenu = new PlayerMenu(sm);
                    playerMenu.openInventory(player, pPage);
                }
                // Handles going to the Next Page
                if(clicked.getItemMeta().getDisplayName().contains(LanguageConfig.getNextPageText())){
                    String str = inventory.getName().replaceAll("\\D+","");
                    int nPage =  Integer.parseInt(str) + 1;
                    if(nPage > maxPages){
                        return;
                    }
                    // Open the menu again for the Next Page
                    PlayerMenu playerMenu = new PlayerMenu(sm);
                    playerMenu.openInventory(player, nPage);
                }
            }

        }

    }

}
