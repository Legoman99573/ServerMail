package com.tyrellplayz.servermail.mysql;

import com.tyrellplayz.servermail.IPlayerData;
import com.tyrellplayz.servermail.IPlayerDataMap;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.PlayerMailData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerMailSQLMap implements IPlayerDataMap{

    private ServerMail sm;

    private HashMap<UUID,IPlayerData> playerMailMap;

    public PlayerMailSQLMap(ServerMail sm) {
        this.sm = sm;
        playerMailMap = new HashMap<>();
        loadPlayerMail();
    }

    public void loadPlayerMail(){
        ServerMail.logger.info("Loading player data. This may take awhile...");
        try{
            Connection con = sm.mySQLHook.getConnection();
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()){
                if(rs.getString(3).equalsIgnoreCase("maildisabled")){
                    continue;
                }else
                if(rs.getString(3).equalsIgnoreCase("playernames")){
                    continue;
                }else{
                    addPlayerMail(UUID.fromString(rs.getString(3)));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
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
        playerMailMap.put(uuid, new PlayerMailSQLData(sm,uuid));
        return true;
    }

    /**
     * Adds a existing PlayerMail to the map
     * @param uuid - The UUID of the player
     * @param playerMail - The existing player mail
     * @return
     */
    public boolean addPlayerMail(UUID uuid, IPlayerData playerMail){
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

    public IPlayerData get(UUID uuid){
        return playerMailMap.get(uuid);
    }

    public boolean contains(UUID uuid){
        return playerMailMap.containsKey(uuid);
    }

    public HashMap<UUID, IPlayerData> getPlayerMailMap() {
        return playerMailMap;
    }
}
