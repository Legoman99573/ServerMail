package com.tyrellplayz.servermail;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public interface IPlayerData {

    UUID getUUID();

    void load();
    void reload();
    void save();

    /**
     * Gets the {@link Mail} from player data
     * @return
     */
    List<Mail> getMailList();

    void _setMailList(List<Mail> mail);
    /**
     * Adds a {@link Mail} to player data. Saves automatically
     * @param mail - {@link Mail} to add
     * @return True if successful, False if not
     */
    boolean addMail(Mail mail);

    /**
     * Removes a {@link Mail} from player data. Saves automatically
     * @param mail - {@link Mail} to remove
     * @return True if successful, False if not
     */
    boolean removeMail(Mail mail);

    /**
     * Removes a {@link Mail} from player data. Saves automatically
     * @param index - Index of message in messages list
     * @return True if successful, False if not
     */
    boolean removeMail(int index);

    /**
     * Gets the players name set in config
     * @return
     */
    String getPlayerName();
    void setPlayerName(String playerName);

    boolean isMailDisabled();
    void setMailDisabled(boolean mailDisabled);

}
