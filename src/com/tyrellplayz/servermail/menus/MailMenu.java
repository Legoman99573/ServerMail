package com.tyrellplayz.servermail.menus;

import com.google.common.collect.Lists;
import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.configs.MainConfig;
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
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class MailMenu extends CustomInventory implements Listener{

    public static String title = "Your Inbox : Page ";
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
        List<Mail> mail = sm.playerMailMap.get(player.getUniqueId()).getMailList();
        int totalMail = 0;
        if(mail != null){
            totalMail = mail.size();
            List<Mail> mailList;
            if(sort == EnumSort.LATEST){
                mailList = Lists.reverse(mail);
            }else{
                mailList = mail;
            }
            int first = 45*(page-1)+1;
            int last = 45*page;
            int slot = 0;
            int i = 1;
            for(Mail message:mailList){
                if((i>=first)&&(i<=last)){
                    if(message.isRead()){
                        sm.getNmsUtils().createBookDisplayForMail(sm, player, inv, slot, ChatColor.YELLOW+ LanguageConfig.getReadMessageText(),message.getMessageSender(),message.getMessage());
                    }else{
                        sm.getNmsUtils().createBookDisplayForMail(sm, player, inv, slot, ChatColor.GREEN+LanguageConfig.getUnReadMessageText(),message.getMessageSender(),message.getMessage());
                    }
                    slot++;
                }
                i++;
            }
        }

        /*
        The Task Bar
         */
        createDisplay(inv, Material.BOOK, 45, ChatColor.GOLD+LanguageConfig.getInformationText(), ChatColor.GREEN+LanguageConfig.getTotalMailText()+" "+ChatColor.GOLD+totalMail);
        createDisplay(inv, Material.BOOK_AND_QUILL, 46,ChatColor.GOLD+LanguageConfig.getSendMailText(), ChatColor.YELLOW+LanguageConfig.getSendMailLoadText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 47, "", MainConfig.getPaneColour());
        createDisplay(inv, Material.PAPER, 48,ChatColor.GOLD+LanguageConfig.getPreviousPageText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 49, "", MainConfig.getPaneColour());
        createDisplay(inv, Material.PAPER, 50, ChatColor.GOLD+LanguageConfig.getNextPageText());
        createDisplayColour(inv, Material.STAINED_GLASS_PANE, 51, "", MainConfig.getPaneColour());
        String sortName;
        if(sort == EnumSort.LATEST){
            sortName = LanguageConfig.getSortLatestMessagesText();
        }else{
            sortName = LanguageConfig.getSortOldMessagesText();
        }
        createDisplay(inv, Material.NAME_TAG, 52, ChatColor.GOLD+LanguageConfig.getSortNext(), ChatColor.GREEN+sortName, ChatColor.GOLD+LanguageConfig.getSortClickText(), String.valueOf(sort.getSortNumber()));
        createDisplay(inv, Material.BARRIER, 53, ChatColor.RED+LanguageConfig.getExitText());

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if(inventory.getName().contains(title)){
            int maxPages;
            if(sm.playerMailMap.get(player.getUniqueId()).getMailList() != null){
                maxPages = (int)Math.ceil(sm.playerMailMap.get(player.getUniqueId()).getMailList().size()/45.0);
            }else{
                maxPages = 1;
            }
            // Stops a player from taking items out
            event.setCancelled(true);
            // Checks if anything was clicked
            if(clicked == null)return;
            // Handles sorting
            if(clicked.getType() == Material.NAME_TAG){
                EnumSort oldSort = EnumSort.getSort(Integer.parseInt(clicked.getItemMeta().getLore().get(2)));
                EnumSort newSort = oldSort.nextSort();
                String str = inventory.getName().replaceAll("\\D+","");
                int page =  Integer.parseInt(str);
                MailMenu mailInventory = new MailMenu(sm);
                mailInventory.openInventory(player, page, newSort);
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
                    MailMenu mailInventory = new MailMenu(sm);
                    mailInventory.openInventory(player, pPage, sm.mailSort.get(player));
                }
                // Handles going to the Next Page
                if(clicked.getItemMeta().getDisplayName().contains(LanguageConfig.getNextPageText())){
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
                BookMeta bookMeta = (BookMeta)clicked.getItemMeta();
                int messageIndex = 0;
                for(Mail message1:sm.playerMailMap.get(player.getUniqueId()).getMailList()){
                    if(bookMeta.getPage(1).contains(message1.getMessage())){
                        messageIndex = sm.playerMailMap.get(player.getUniqueId()).getMailList().indexOf(message1);
                    }
                }
                if(!sm.playerMailMap.get(player.getUniqueId()).getMailList().get(messageIndex).isRead()){
                    sm.playerMailMap.get(player.getUniqueId()).getMailList().get(messageIndex).read();
                    sm.getServer().getScheduler().runTaskAsynchronously(sm, () -> sm.playerMailMap.get(player.getUniqueId()).save());
                }
                player.closeInventory();
                Utils.openBook(player, clicked);
            }
        }

    }

}
