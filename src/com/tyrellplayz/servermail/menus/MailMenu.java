package com.tyrellplayz.servermail.menus;

import com.google.common.collect.Lists;
import com.tyrellplayz.servermail.Messages;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MailMenu extends CustomInventory implements Listener{

    private static String title = "Your Inbox : Page ";
    private int rows = 6;

    private static Inventory inv;
    private ServerMail sm;

    public MailMenu(ServerMail sm) {
        this.sm = sm;
    }

    public void openInventory(Player player, int page, EnumSort sort){
        inv = Bukkit.createInventory(null,getSize(rows),title+page);
        inv.setMaxStackSize(1);

        if(sm.mailSort.containsKey(player)){
            sm.mailSort.remove(player);
        }
        sm.mailSort.put(player, sort);

        /*
        Messages
         */
        Messages messages = sm.messagesMap.get(player.getUniqueId());
        int totalMessages = 0;
        if(messages != null){
            if(messages.getMessages() != null){
                totalMessages = messages.getMessages().size();
                List<String> messagesList;
                if(sort == EnumSort.LATEST){
                    messagesList = Lists.reverse(messages.getMessages());
                }else{
                    messagesList = messages.getMessages();
                }
                int first = 45*(page-1)+1;
                int last = 45*page;
                int slot = 0;
                int i = 1;
                for(String message:messagesList){
                    if((i>=first)&&(i<=last)){
                        String[] messageSplit = message.split("/");
                        String sender = messageSplit[0];
                        String time = "";
                        String text = messageSplit[2];
                        sm.getNmsUtils().createBookDisplayForMail(sm, player, inv, slot, "Click to view...",sender,text);
                        slot++;
                    }
                    i++;
                }
            }
        }

        /*
        The Task Bar
         */
        createDisplay(inv, Material.BOOK, 45, ChatColor.GOLD+"Information", ChatColor.GREEN+"Total mail: "+ChatColor.GOLD+totalMessages);
        createDisplay(inv, Material.BOOK_AND_QUILL, 46,ChatColor.GOLD+"Send mail");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 47, "", (byte)7);
        createDisplay(inv, Material.PAPER, 48,ChatColor.GOLD+"Previous page");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 49, "", (byte)7);
        createDisplay(inv, Material.PAPER, 50, ChatColor.GOLD+"Next page");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 51, "", (byte)7);
        createDisplay(inv, Material.NAME_TAG, 52, ChatColor.GOLD+"Sort", ChatColor.GREEN+sort.getSortName(), ChatColor.GOLD+"Click to change");
        createDisplay(inv, Material.BARRIER, 53, ChatColor.RED+"Exit");

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if(inventory.getName().contains(title)){
            int maxPages = (int)Math.ceil(sm.messagesMap.get(player.getUniqueId()).getMessages().size()/45.0);
            // Stops a player from taking items out
            event.setCancelled(true);
            // Checks if anything was clicked
            if(clicked == null)return;
            // Handles sorting
            if(clicked.getType() == Material.NAME_TAG){
                EnumSort oldSort = EnumSort.getSort(ChatColor.stripColor(clicked.getItemMeta().getLore().get(0)));
                EnumSort newSort = oldSort.nextSort();
                String str = inventory.getName().replaceAll("\\D+","");
                int page =  Integer.parseInt(str);
                MailMenu mailInventory = new MailMenu(sm);
                mailInventory.openInventory(player, page, newSort);
            }
            if(clicked.getType() == Material.PAPER){
                // Handles going to a Previous Page
                if(clicked.getItemMeta().getDisplayName().contains("Previous")){
                    String str = inventory.getName().replaceAll("\\D+","");
                    int pPage =  Integer.parseInt(str) - 1;
                    if(pPage < 1){
                        return;
                    }
                    // Open the menu again for the Previous Page
                    MailMenu mailInventory = new MailMenu(sm);
                    mailInventory.openInventory(player, pPage, sm.mailSort.get(player));
                }
                // Handles going to the Next Page
                if(clicked.getItemMeta().getDisplayName().contains("Next")){
                    String str = inventory.getName().replaceAll("\\D+","");
                    int nPage =  Integer.parseInt(str) + 1;
                    if(nPage > maxPages){
                        return;
                    }
                    // Open the menu again for the Next Page
                    MailMenu mailInventory = new MailMenu(sm);
                    mailInventory.openInventory(player, nPage, sm.mailSort.get(player));
                }
            }
            // Exit menu
            if(clicked.getType() == Material.BARRIER){
                player.closeInventory();
            }
            // Exit menu
            if(clicked.getType() == Material.BOOK_AND_QUILL){
                PlayerMenu playerMenu = new PlayerMenu(sm);
                playerMenu.openInventory(player, 1);
            }
            // Handles viewing messages
            if(clicked.getType() == Material.WRITTEN_BOOK){
                player.closeInventory();
                Utils.openBook(player, clicked);
            }
        }

    }

}
