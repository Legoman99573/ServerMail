package com.tyrellplayz.servermail.menus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class CustomInventory {

    protected int getSize(int rows){
        int slots;
        slots = 9*rows;
        if(slots<9) slots = 9;
        if(slots>54) slots = 54;
        return slots;
    }

    protected void createDisplay(Inventory inventory, Material material, int slot, String name){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void createDisplayColour(Inventory inventory, Material material, int slot, String name, byte data){
        ItemStack item = new ItemStack(material, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void createDisplay(Inventory inventory, Material material, int slot, String name, String... lore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void createHeadDisplay(Inventory inventory, OfflinePlayer player, int slot, String... lore){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.GOLD+player.getName());
        meta.setDisplayName(player.getName());
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void addHeadDisplay(Inventory inventory, OfflinePlayer player, String... lore){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.GOLD+player.getName());
        meta.setDisplayName(player.getName());
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inventory.addItem(item);
    }

    protected void createHeadDisplay(Inventory inventory, Player player, int slot, String... lore){
        ItemStack item = new ItemStack(Material.SKULL);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(player.getName());
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void createBookDisplay(Inventory inventory, int slot, String title, String author, String... pages){
        BookMeta bookMeta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        bookMeta.setPages(pages);
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        item.setItemMeta(bookMeta);
        inventory.setItem(slot, item);
    }

    protected void addBookDisplay(Inventory inventory, String title, String author, String... pages){
        BookMeta bookMeta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        bookMeta.setPages(pages);
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        item.setItemMeta(bookMeta);
        inventory.addItem(item);
    }

}
