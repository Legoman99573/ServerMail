package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.List;

/**
 * Creates/Loads the main config file of the plugin
 */
public class MainConfig{

    private ServerMail sm;

    public MainConfig(ServerMail sm) {
        this.sm = sm;
        load();
        MainConfigUpdater updater = new MainConfigUpdater(sm);
        updater.checkForUpdate();
        reload();
    }

    public void load(){
        if(!sm.getDataFolder().exists())sm.getDataFolder().mkdirs();
        File file = new File(sm.getDataFolder(), "config.yml");
        if(!file.exists())sm.saveDefaultConfig();
    }

    public void reload(){
        try{
            updateChecker = sm.getConfig().getBoolean("updateChecker");
            setToastNotifications(sm.getConfig().getBoolean("toastNotifications"));
            updateMessageOnJoin = sm.getConfig().getBoolean("updateMessageOnJoin");
            setPaneColour((byte)sm.getConfig().getInt("paneColour"));
            setStorageType(sm.getConfig().getString("storageType"));
            itemPackages = sm.getConfig().getBoolean("itemPackages");
            setMoneyPackages(sm.getConfig().getBoolean("moneyPackages"));
            blockedWords = sm.getConfig().getStringList("blockedWords");
            sqlHost = sm.getConfig().getString("sqlHost");
            sqlDatabase = sm.getConfig().getString("sqlDatabase");
            sqlUsername = sm.getConfig().getString("sqlUsername");
            sqlPassword = sm.getConfig().getString("sqlPassword");
            sqlPort = sm.getConfig().getInt("sqlPort");
        }catch (Exception ex){
            LogMessagesUtil.errorMessage("Error while enabling","Config","Could not set data from 'config.yml'. Please check that no data in the file equals null/nothing or data is set to the right type of data");
            ex.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
        }
    }

    private static boolean updateChecker;
    public static boolean getUpdateChecker(){
        return updateChecker;
    }

    private static boolean toastNotifications;

    public static void setToastNotifications(boolean toastNotifications) {
        String version;
        try{
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        }catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException){
            MainConfig.toastNotifications = false;
            return;
        }
        if(version.equals("v1_12_R1")) {
            MainConfig.toastNotifications = toastNotifications;
            return;
        }
        MainConfig.toastNotifications = false;
    }

    public static boolean getToastNotifications() {
        return MainConfig.toastNotifications;
    }

    private static String storageType;
    private void setStorageType(String value){
        if(value.equalsIgnoreCase("yml")){
            storageType = "yml";
        }else if(value.equalsIgnoreCase("mysql")){
            storageType = "mysql";
        }else{
            LogMessagesUtil.errorMessage("Error while loading 'config.yml'", "Configs", "'storageType' is invalid. Please choose 'yml' or 'mysql'");
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
        }
    }
    public static String getStorageType() {
        return storageType;
    }

    private static Byte paneColour;
    private void setPaneColour(Byte paneColour) {
        if(paneColour < (byte)0 || paneColour > (byte)15){
            LogMessagesUtil.errorMessage("Error while enabling","Configs","The data set at 'paneColour' is invalid. Please set to a valid dye damage value");
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
            return;
        }else{
            MainConfig.paneColour = paneColour;
        }
    }
    public static Byte getPaneColour() {
        return paneColour;
    }

    private static boolean itemPackages;
    public static boolean getItemPackages() {
        return itemPackages;
    }

    private static boolean moneyPackages;
    public static boolean getMoneyPackages() {
        return moneyPackages;
    }
    private void setMoneyPackages(boolean value) {
        if(ServerMail.getEcon()==null){
            MainConfig.moneyPackages = false;
        }else{
            MainConfig.moneyPackages = value;
        }
    }

    private static List<String> blockedWords;
    public static List<String> getBlockedWords() {
        return blockedWords;
    }

    private static boolean updateMessageOnJoin;
    public static boolean getUpdateMessageOnJoin() {
        return updateMessageOnJoin;
    }

    private static String sqlHost, sqlDatabase, sqlUsername, sqlPassword;
    private static int sqlPort;

    public static String getSQLHost() { return sqlHost; }

    public static String getSQLDatabase() { return sqlDatabase; }

    public static String getSQLUsername() { return sqlUsername; }

    public static String getSQLPassword() { return sqlPassword; }

    public static int getSQLPort() { return sqlPort; }
}
