package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.ServerMail;

import java.io.File;

/**
 * Creates/Loads the main config file of the plugin
 */
public class MainConfig {

    /* DEBUG */
    private boolean resetOnEnabled = true;

    private ServerMail sm;

    public MainConfig(ServerMail sm) {
        this.sm = sm;
        load();
    }

    private void load(){
        if(!sm.getDataFolder().exists())sm.getDataFolder().mkdirs();
        File file = new File(sm.getDataFolder(), "config.yml");
        if(!file.exists() || resetOnEnabled)sm.saveDefaultConfig();
    }

}
