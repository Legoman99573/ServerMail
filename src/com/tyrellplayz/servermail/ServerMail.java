package com.tyrellplayz.servermail;

import com.tyrellplayz.servermail.commands.CommandDeleteMail;
import com.tyrellplayz.servermail.commands.CommandMail;
import com.tyrellplayz.servermail.commands.CommandReceiveMail;
import com.tyrellplayz.servermail.commands.CommandSendMail;
import com.tyrellplayz.servermail.configs.LanguageConfig;
import com.tyrellplayz.servermail.configs.MainConfig;
import com.tyrellplayz.servermail.events.ChatEvents;
import com.tyrellplayz.servermail.events.PlayerEvents;
import com.tyrellplayz.servermail.menus.EnumSort;
import com.tyrellplayz.servermail.menus.MailMenu;
import com.tyrellplayz.servermail.menus.MailSendMenu;
import com.tyrellplayz.servermail.menus.PlayerMenu;
import com.tyrellplayz.servermail.mysql.MySQLHook;
import com.tyrellplayz.servermail.mysql.PlayerMailSQLMap;
import com.tyrellplayz.servermail.nms.*;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import com.tyrellplayz.servermail.utils.SignMenu;
import com.tyrellplayz.servermail.utils.ToastMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * The main class for ServerMail. ServerMail is a gui based mail system for a spigot server which allows players
 * to send messages to online and offline players.
 * @author TyrellPlayz
 * @version 0.2.1
 */
public class ServerMail extends JavaPlugin implements IPlugin{

    private static Economy econ = null;

    public static PluginDescriptionFile pdf;

    public static Logger logger;

    private static boolean essentials = false;

    private List<UUID> mailDisabledPlayers = new ArrayList<>();
    public IPlayerDataMap playerMailMap;
    public static List<OfflinePlayer> offlinePlayers;

    /**
     * Sender, Receiver
     */
    public HashMap<Player,OfflinePlayer> sendMessageMap = new HashMap<>();
    public HashMap<Player,OfflinePlayer> sendItemMap = new HashMap<>();
    public HashMap<Player,OfflinePlayer> sendMoneyMap = new HashMap<>();
    public HashMap<Player,EnumSort> mailSort = new HashMap<>();
    public HashMap<Player,Double> sendMoneyAmountMap = new HashMap<>();

    private MainConfig mainConfig;
    private LanguageConfig languageConfig;
    public MySQLHook mySQLHook;

    private NMSUtils nmsUtils;
    public SignMenu signMenu;

    private static String serverVersion;
    public ToastMessage toastMessage;

    @Override
    public void onLoad() {
        pdf = getDescription();
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        LogMessagesUtil.enableMessage();
        if(!Bukkit.getOnlineMode()){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"Server must be in online mode!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerHooks();
        languageConfig = new LanguageConfig(this);
        if(!setupNMSUtils()){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"Server is not the right version. Plugin only works on versions 1.9.* - 1.12.*");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        mainConfig = new MainConfig(this);
        if(MainConfig.getStorageType().equalsIgnoreCase("yml")){
            playerMailMap = new PlayerMailMap(this);
        }else if(MainConfig.getStorageType().equalsIgnoreCase("mysql")){
            mySQLHook = new MySQLHook(this);
            mySQLHook.createNewPlayerData(Bukkit.getServer().getOfflinePlayer("TyrellPlayz").getUniqueId());
            playerMailMap = new PlayerMailSQLMap(this);
        }

        if(MainConfig.getUpdateChecker()){
            Updater updater = new Updater(this, 55429);
            try {
                if (updater.checkForUpdates())
                    LogMessagesUtil.updateMessage(updater.getLatestVersion(),updater.getResourceURL());
            } catch (Exception e) {
                logger.warning("Could not check for updates!");
            }
        }
        offlinePlayers = Arrays.asList(getServer().getOfflinePlayers());
        /* DEBUG
        offlinePlayers = new ArrayList<>();
        for(int x = 0; x < 100; x++){
            offlinePlayers.add(Bukkit.getServer().getOfflinePlayer("TyrellPlayz"));
        }
        */
        offlinePlayers.sort(Comparator.comparing(playerOne -> (playerOne).getName()));

        signMenu = new SignMenu(this);

        registerEvents();
        registerCommands();

        MailMenu.title = LanguageConfig.getMailMenuTitle()+" ";
        PlayerMenu.title = LanguageConfig.getPlayerMenuTitle()+" ";
        MailSendMenu.title = LanguageConfig.getMailSendMenuTitle();

        logger.info("Server Mail Enabled");
    }

    @Override
    public void onDisable() {
        if(mySQLHook != null)mySQLHook.mySQLCloseConnection();
        logger.info("Server Mail Disabled");
    }

    @Override
    public void registerCommands() {
        getCommand("mail").setExecutor(new CommandMail(this));
        getCommand("maildisable").setExecutor(new CommandMail(this));
        getCommand("deletemail").setExecutor(new CommandDeleteMail(this));
        getCommand("sendmail").setExecutor(new CommandSendMail(this));
        getCommand("receivemail").setExecutor(new CommandReceiveMail(this));
    }

    @Override
    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new MailMenu(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMenu(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatEvents(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new MailSendMenu(this),this);
    }

    @Override
    public void registerTasks() {

    }

    private void registerHooks(){
        logger.info("Checking for ProtocolLib...");
        if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"No ProtocolLib dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        logger.info("Checking for Vault...");
        if (!setupEconomy() ) {
            logger.warning("No Vault dependency found! Money packages are disabled");
        }
        if(getServer().getPluginManager().isPluginEnabled("Essentials")) essentials = true;
    }

    // Vault
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return false; }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    // This method will setup the NMSUtils class and return true if the server is running a
    // version compatible with our NMS classes.
    // If the server is not compatible, it will return false!
    private boolean setupNMSUtils(){
        String version;
        try{
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        }catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException){
            return false;
        }
        //Log.info("Your server is running version " + version);
        if(version.equals("v1_12_R1")){
            //server is running 1.12.* so we need to use the 1.12 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_12_R1();
            toastMessage = new ToastMessage("new_mail", LanguageConfig.getNewMailText(), "chest_minecart", this);
            serverVersion = version;
        }else if(version.equals("v1_11_R1")){
            //server is running 1.11.* so we need to use the 1.11 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_11_R1();
            serverVersion = version;
        }else if(version.equals("v1_10_R1")){
            //server is running 1.10.* so we need to use the 1.10 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_10_R1();
            serverVersion = version;
        }else if(version.equals("v1_9_R1")){
            //server is running 1.9 - 1.9.2 so we need to use the 1.9 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_9_R1();
            serverVersion = version;
        }else if(version.equals("v1_9_R2")){
            //server is running 1.9.4 so we need to use the 1.9 R2 NMSUtils class
            nmsUtils = new NMSUtils_1_9_R2();
            serverVersion = version;
        }
        // This will return true if the server version was compatible with one of our NMS classes
        // because if it is, our actionbar would not be null
        return nmsUtils != null;
    }
    public NMSUtils getNmsUtils() { return nmsUtils; }


}
