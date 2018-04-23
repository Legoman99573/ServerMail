package com.tyrellplayz.servermail;

import com.tyrellplayz.servermail.configs.PlayerMailData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMailMap {

    private ServerMail sm;

    private HashMap<UUID,PlayerMailData> playerMailMap;

    public PlayerMailMap(ServerMail sm) {
        this.sm = sm;
        playerMailMap = new HashMap<>();
        loadPlayerMail();
    }

    public void saveAll(){
        for(UUID uuid:playerMailMap.keySet()){
            playerMailMap.get(uuid).save();
        }
    }

    private void loadPlayerMail(){
        ServerMail.logger.info("Loading player data. This may take awhile...");
        for(OfflinePlayer offlinePlayer: Bukkit.getServer().getOfflinePlayers()){
            UUID uuid = offlinePlayer.getUniqueId();
            addPlayerMail(uuid);
        }
    }

    /**
     * Adds a new PlayerMail to the map
     * @param uuid - The UUID of the player
     * @return
     */
    public boolean addPlayerMail(UUID uuid){
        if(playerMailMap.containsKey(uuid)){
            return false;
        }
        playerMailMap.put(uuid, new PlayerMailData(sm,uuid));
        return true;
    }

    /**
     * Adds a existing PlayerMail to the map
     * @param uuid - The UUID of the player
     * @param playerMail - The existing player mail
     * @return
     */
    public boolean addPlayerMail(UUID uuid, PlayerMailData playerMail){
        if(playerMailMap.containsKey(uuid)){
            return false;
        }
        playerMailMap.put(uuid, playerMail);
        return true;
    }

    /**
     * Removes a PlayerMail from the map
     * @param uuid - The UUID of the player
     * @return
     */
    public boolean removePlayerMail(UUID uuid){
        if(playerMailMap.containsKey(uuid)){
            playerMailMap.remove(uuid);
            return true;
        }
        return false;
    }

    public PlayerMailData get(UUID uuid){
        return playerMailMap.get(uuid);
    }

    public boolean contains(UUID uuid){
        return playerMailMap.containsKey(uuid);
    }

    public HashMap<UUID, PlayerMailData> getPlayerMailMap() {
        return playerMailMap;
    }
}
