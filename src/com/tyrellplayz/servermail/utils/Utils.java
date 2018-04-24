package com.tyrellplayz.servermail.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.tyrellplayz.servermail.InvalidItemStackException;
import com.tyrellplayz.servermail.configs.MainConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static int booleanToInt(boolean bool){
        if(bool)return 1;
        return 0;
    }

    /**
     * Turns a {@link String} into an {@link ItemStack}. Format: Type,Amount,Data.
     * @param value - {@link String} value to be converted.
     * @return ItemStack
     * @throws InvalidItemStackException
     */
    public static ItemStack stringToItemStack(String value)throws InvalidItemStackException{
        String[] item = value.split("/");
        if(item.length<3 || item.length>3)throw new InvalidItemStackException("ItemStack is not in right format");
        Material material = Material.getMaterial(item[0]);
        if(material==null)throw new InvalidItemStackException("Can't create ItemStack with invalid Material");
        int amount = Integer.parseInt(item[1]);
        if(amount<1 || amount>64)throw new InvalidItemStackException("Can't create ItemStack with amount under 1 or over 64");
        byte data = Byte.parseByte(item[2]);
        HashMap<Enchantment, Integer> map = new HashMap<>();
        return new ItemStack(material,amount,data);
    }

    /**
     * Turns a {@link ItemStack} into an {@link String}. Format: Type,Amount,Data.
     * @param itemStack - {@link ItemStack} to be converted.
     * @return String
     */
    public static String itemStackToString(ItemStack itemStack){
        String material = itemStack.getType().name();
        int amount = itemStack.getAmount();
        byte data = itemStack.getData().getData();
        return material+"/"+amount+"/"+data/*/"+itemStack.getEnchantments()*/;
    }

    public static String removeBlockedWords(String message){
        for(String blockedWord: MainConfig.getBlockedWords()){
            if(message.contains(blockedWord)){
                String s = "";
                for(int i = 0;i < blockedWord.length();i++){
                    s=s+"*";
                }
                message = message.replaceAll(blockedWord, s);
            }
        }
        return message;
    }

    /**
     * Opens a book
     * @param player - The {@link Player} to open the book
     * @param book - The book to open
     */
    public static void openBook(Player player, ItemStack book){
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);
        try{
            PacketContainer pc = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
            pc.getModifier().writeDefaults();
            ByteBuf bf = Unpooled.buffer(256);
            bf.setByte(0, (byte)0); // Open book from hand: 0, Open from offhand: 1
            bf.writeInt(1);
            pc.getModifier().write(1, MinecraftReflection.getPacketDataSerializer(bf));
            if(book.getType() == Material.BOOK_AND_QUILL){
                pc.getStrings().write(0, "MC|BEdit");
            }else{
                pc.getStrings().write(0, "MC|BOpen");
            }
            ProtocolLibrary.getProtocolManager().sendServerPacket(player,pc);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        player.getInventory().setItem(slot,old);
    }

}
