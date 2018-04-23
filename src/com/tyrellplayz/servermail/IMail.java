package com.tyrellplayz.servermail;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface IMail {

    /**
     * Sends mail to a player
     * @param receiver - Player to receive the mail
     * @param message - The message that will be sent to the receiver
     * @return True if successful False if not
     */
    //boolean sendMail(Player receiver, String message);

    /**
     * Sends mail to a offline player
     * @param receiver - Player to receive the mail
     * @param message - The message that will be sent to the receiver
     * @return True if successful False if not
     */
    boolean sendMail(OfflinePlayer receiver, String message);

    /**
     * Sends mail to a offline player with an item package
     * @param receiver - Player to receive the mail
     * @param message - The message that will be sent to the receiver
     * @param itemStack - {@link ItemStack} to be sent
     * @return True if successful False if not
     */
    boolean sendMail(OfflinePlayer receiver, String message, ItemStack itemStack);

    /**
     * Sends mail to a offline player with a money package
     * @param receiver - Player to receive the mail
     * @param message - The message that will be sent to the receiver
     * @param money - Money to be sent
     * @return True if successful False if not
     */
    boolean sendMail(OfflinePlayer receiver, String message, Double money);

    /**
     * Sends mail to a player via UUID
     * @param receiver - Player UUID to receive the mail
     * @param message - The message that will be sent to the receiver
     * @return True if successful False if not
     */
   //boolean sendMail(UUID receiver, String message);

}
