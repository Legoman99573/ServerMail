package com.tyrellplayz.servermail.configs;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public interface IConfig {

    String getFileLocation();
    String getFileName();
    FileConfiguration getConfiguration();
    UUID getUUID();

    void load();
    void reload();

}
