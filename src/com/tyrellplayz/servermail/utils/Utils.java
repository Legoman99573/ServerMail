package com.tyrellplayz.servermail.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {




    /**
     * Opens a book
     * @param player - The player to open the book
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
