package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.List;

public class MainConfigUpdater {

    private static String thisConfigVersion = "0.5";

    private ServerMail sm;
    private String configVersion;
    private boolean configAutoUpdate;

    MainConfigUpdater(ServerMail sm) {
        this.sm = sm;
        configVersion = sm.getConfig().getString("configVersion");
        configAutoUpdate = sm.getConfig().getBoolean("configAutoUpdate");
    }

    /**
     * Checks if 'config.yml' needs an update
     */
    public void checkForUpdate(){
        //noinspection StatementWithEmptyBody
        if(configVersion.equalsIgnoreCase(thisConfigVersion)){
            // No update is needed
        }else{
            // Update is needed
            if(configAutoUpdate){
                // Update config
                ServerMail.logger.info("'config.yml' is updating...'");
                updateConfig();
            }else{
                // Don't automatically update config
                LogMessagesUtil.errorMessage("Error while enabling","Configs","'config.yml' needs to be updated");
                Bukkit.getServer().getPluginManager().disablePlugin(sm);
            }
        }
    }

    /**
     * Updates the config
     */
    private void updateConfig(){
        // Get data from old config
        boolean updateChecker = sm.getConfig().getBoolean("updateChecker");
        //String storageType = sm.getConfig().getString("storageType");
        boolean toastNotifications = sm.getConfig().getBoolean("toastNotifications");
        boolean updateMessageOnJoin = sm.getConfig().getBoolean("updateMessageOnJoin");
        byte paneColour = (byte) sm.getConfig().getInt("paneColour");
        boolean itemPackages = sm.getConfig().getBoolean("itemPackages");
        boolean moneyPackages = sm.getConfig().getBoolean("moneyPackages");
        List<String> blockedWords = sm.getConfig().getStringList("blockedWords");

        String sqlHost = sm.getConfig().getString("sqlHost");
        String sqlDatabase = sm.getConfig().getString("sqlDatabase");
        String sqlUsername = sm.getConfig().getString("sqlUsername");
        String sqlPassword = sm.getConfig().getString("sqlPassword");
        int sqlPort = sm.getConfig().getInt("sqlPort");

        // Replace old config with new config
        File file = new File(sm.getDataFolder(),"config.yml");
        if(file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();
        sm.saveDefaultConfig();

        // Set data in new config from old config
        sm.getConfig().set("configVersion", thisConfigVersion);
        sm.getConfig().set("configAutoUpdate",configAutoUpdate);
        sm.getConfig().set("updateChecker", updateChecker);
        //sm.getConfig().set("storageType", storageType);
        sm.getConfig().set("toastNotifications", toastNotifications);
        sm.getConfig().set("updateMessageOnJoin", updateMessageOnJoin);
        sm.getConfig().set("paneColour",(int) paneColour);
        sm.getConfig().set("itemPackages", itemPackages);
        sm.getConfig().set("moneyPackages", moneyPackages);
        sm.getConfig().set("blockedWords", blockedWords);

        sm.getConfig().set("sqlHost", sqlHost);
        sm.getConfig().set("sqlDatabase", sqlDatabase);
        sm.getConfig().set("sqlUsername", sqlUsername);
        sm.getConfig().set("sqlPassword", sqlPassword);
        sm.getConfig().set("sqlPort", sqlPort);

        // Save new config with new data
        sm.saveConfig();
    }

}
