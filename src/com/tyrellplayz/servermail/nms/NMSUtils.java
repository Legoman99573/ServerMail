package com.tyrellplayz.servermail.nms;

import com.tyrellplayz.servermail.ServerMail;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface NMSUtils {

    public void createBookDisplayForMail(ServerMail sm, Player player, Inventory inventory, int slot, String title, String sender, String message);

}
