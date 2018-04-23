package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Create/Loads the storage config of the plugin. The storage plugin holds the mail from players, ect...
 */
public class StorageConfig {

    /* DEBUG */
    private boolean resetOnEnabled = false;

    private ServerMail sm;
    public File file;
    public FileConfiguration fileConfiguration;

    public StorageConfig(ServerMail sm) {
        this.sm = sm;
        file = new File(sm.getDataFolder(), "storage.yml");
        fileConfiguration = new YamlConfiguration();
        load();
    }

    private void load(){
        try{
            if(!sm.getDataFolder().exists())sm.getDataFolder().mkdirs();
            if(!file.exists() || resetOnEnabled)file.createNewFile();
            fileConfiguration.load(file);
        }catch (Exception ex){
            LogMessagesUtil.errorMessage("Error while enabling plugin", "Configs", "Unable to load 'storage.yml'");
            ex.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
            return;
        }
    }

}
