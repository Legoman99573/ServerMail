package com.tyrellplayz.servermail;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class Mail {

    private boolean read;

    private String messageSender;
    private String message;
    @Nullable
    private ItemStack itemStack;
    @Nullable
    private boolean itemReceived;

    private double money;
    @Nullable
    private boolean moneyReceived;

    /**
     * A normal message
     * @param messageSender - The player that sent the message
     * @param message - The message
     */
    public Mail(String messageSender, String message, boolean read) {
        this.messageSender = messageSender;
        this.message = message;
        this.read = read;
    }

    /**
     * A message with a gift of an item/block
     * @param messageSender - The player that sent the message
     * @param message - The message
     * @param itemStack - The item/block
     */
    public Mail(String messageSender, String message, boolean read, ItemStack itemStack, boolean itemReceived) {
        this.messageSender = messageSender;
        this.message = message;
        this.itemStack = itemStack;
        this.read = read;
    }

    /**
     * A message with a gift of money
     * @param messageSender - The player that sent the message
     * @param message - The message
     * @param money - How much money
     */
    public Mail(String messageSender, String message, boolean read, double money, boolean moneyReceived) {
        this.messageSender = messageSender;
        this.message = message;
        this.money = money;
        this.read = read;
    }

    public String getMessageSender() {
        return messageSender;
    }
    public String getMessage() {
        return message;
    }

    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }
    public boolean isItemReceived() { return itemReceived; }
    public boolean transferItem(Player player){
        if(hasItemStack() && !itemReceived){
            player.getInventory().addItem(getItemStack());
            itemReceived=true;
            return true;
        }
        return false;
    }
    public boolean hasItemStack(){ return itemStack != null; }

    public double getMoney() {
        return money;
    }
    public boolean transferMoney(OfflinePlayer player){
        if(hasMoney() && !moneyReceived){
            ServerMail.getEcon().depositPlayer(player, money);
            moneyReceived=true;
            return true;
        }
        return false;
    }
    public boolean isMoneyReceived() { return moneyReceived; }
    public boolean hasMoney(){return !(money == 0); }

    public boolean isRead(){ return read; }
    public void read() { read = true; }

}
