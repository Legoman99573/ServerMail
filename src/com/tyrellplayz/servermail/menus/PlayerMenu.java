package com.tyrellplayz.servermail.menus;

import com.tyrellplayz.servermail.Messages;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.List;

public class PlayerMenu extends CustomInventory implements Listener{

    private static String title = "Pick player : Page ";
    private int rows = 6;

    private static Inventory inv;
    private ServerMail sm;

    public PlayerMenu(ServerMail sm) {
        this.sm = sm;
    }

    public void openInventory(Player player, int page){
        inv = Bukkit.createInventory(null,getSize(rows),title+page);
        inv.setMaxStackSize(1);

        List<OfflinePlayer> playerList = Arrays.asList(Bukkit.getServer().getOfflinePlayers());
        /*
        Players
         */
        int first = 45*(page-1)+1;
        int last = 45*page;
        int i = 1;
        int slot = 0;
        for(OfflinePlayer offlinePlayer:playerList){
            if((i>=first)&&(i<=last)){
                createHeadDisplay(inv, offlinePlayer,slot, "Send mail");
                slot++;
            }
            i++;
        }
        /*
        The Task Bar
         */
        createDisplay(inv, Material.SIGN, 45,ChatColor.GOLD+"Enter a name", ChatColor.RED+"Coming soon");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 46, "", (byte)7);
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 47, "", (byte)7);
        createDisplay(inv, Material.PAPER, 48,ChatColor.GOLD+"Previous page");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 49, "", (byte)7);
        createDisplay(inv, Material.PAPER, 50, ChatColor.GOLD+"Next page");
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 51, "", (byte)7);
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 52, "", (byte)7);
        createDisplay(inv, Material.BARRIER, 53, ChatColor.RED+"Back");

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
            // Handles clicked player
            if(clicked.getType() == Material.SKULL_ITEM || clicked.getType() == Material.SKULL){
                Log.info("Started mail");
                // Opens book to send message to clicked player
                OfflinePlayer receiver = Bukkit.getServer().getOfflinePlayer(clicked.getItemMeta().getDisplayName());
                sm.sendMessageMap.put(player, receiver);
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN+"Type message in chat. To cancel type 'cancel'");
            }
            // Goes back to mail menu
            if(clicked.getType() == Material.BARRIER){
                MailMenu mailMenu = new MailMenu(sm);
                mailMenu.openInventory(player, 1, sm.mailSort.get(player));
            }
            // Handles going to a Previous Page
            if(clicked.getType().getData().getName().equalsIgnoreCase("Previous page")){
                int pPage =  Integer.parseInt(inventory.getName()) - 1;
                if(pPage < 1){
                    return;
                }
                // Open the menu again for the Previous Page
                PlayerMenu playerMenu = new PlayerMenu(sm);
                playerMenu.openInventory(player, pPage);
            }
            // Handles going to the Next Page
            if(clicked.getType().getData().getName().equalsIgnoreCase("Next page")){
                List<OfflinePlayer> playerList = Arrays.asList(Bukkit.getServer().getOfflinePlayers());
                int nPage =  Integer.parseInt(inventory.getName()) + 1;
                int playerSize = playerList.size();
                if(nPage > playerSize/45){
                    return;
                }
                // Open the menu again for the Next Page
                PlayerMenu playerMenu = new PlayerMenu(sm);
                playerMenu.openInventory(player, nPage);
            }
        }

    }

}
