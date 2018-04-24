package com.tyrellplayz.servermail;

import com.tyrellplayz.servermail.configs.PlayerMailData;

import java.util.HashMap;
import java.util.UUID;

public interface IPlayerDataMap {

    void loadPlayerMail();

    /**
     * Adds a new PlayerMail to the map
     * @param uuid - The UUID of the player
     * @return
     */
    boolean addPlayerMail(UUID uuid);

    /**
     * Adds a existing PlayerMail to the map
     * @param uuid - The UUID of the player
     * @param playerMail - The existing player mail
     * @return
     */
    boolean addPlayerMail(UUID uuid, IPlayerData playerMail);

    /**
     * Removes a PlayerMail from the map
     * @param uuid - The UUID of the player
     * @return
     */
    boolean removePlayerMail(UUID uuid);

    IPlayerData get(UUID uuid);

    boolean contains(UUID uuid);

    HashMap<UUID, IPlayerData> getPlayerMailMap();

}
