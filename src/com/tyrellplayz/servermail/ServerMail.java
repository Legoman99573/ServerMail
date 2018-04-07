package com.tyrellplayz.servermail;

import com.tyrellplayz.servermail.commands.CommandDeleteMail;
import com.tyrellplayz.servermail.commands.CommandMail;
import com.tyrellplayz.servermail.commands.CommandReplyMail;
import com.tyrellplayz.servermail.configs.MainConfig;
import com.tyrellplayz.servermail.configs.StorageConfig;
import com.tyrellplayz.servermail.events.ChatEvents;
import com.tyrellplayz.servermail.events.PlayerEvents;
import com.tyrellplayz.servermail.menus.EnumSort;
import com.tyrellplayz.servermail.menus.MailMenu;
import com.tyrellplayz.servermail.menus.PlayerMenu;
import com.tyrellplayz.servermail.nms.*;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/**
 * The main class for ServerMail. ServerMail is a gui based mail system for a spigot server which allows players
 * to send messages to online and offline players.
 * @author TyrellPlayz
 * @version 0.1.0
 */
public class ServerMail extends JavaPlugin implements IPlugin{

    public static PluginDescriptionFile pdf;

    public List<UUID> mailDisabledPlayers = new ArrayList<>();
    public HashMap<UUID,Messages> messagesMap = new HashMap<>();

    /**
     * Sender, Receiver
     */
    public HashMap<Player,OfflinePlayer> sendMessageMap = new HashMap<>();

    public HashMap<Player,EnumSort> mailSort = new HashMap<>();

    private MainConfig mainConfig = new MainConfig(this);
    private StorageConfig storageConfig = new StorageConfig(this);

    private NMSUtils nmsUtils;

    @Override
    public void onLoad() {
        pdf = getDescription();
    }

    @Override
    public void onEnable() {
        LogMessagesUtil.enableMessage();
        if(!Bukkit.getOnlineMode()){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"Server must be in online mode");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"ProtocolLib is not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!setupNMSUtils()){
            LogMessagesUtil.errorMessage("Error while enabling plugin",null,"Server is not the right version. Plugin only works on versions 1.9.* - 1.12.*");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadMessages();
        loadMailDisabledPlayers();

        registerEvents();
        registerCommands();

        Log.info("Server Mail Enabled");
    }

    @Override
    public void onDisable() {
        saveMessages();
        saveMailDisabledPlayers();
        Log.info("Server Mail Disabled");
    }

    @Override
    public void registerCommands() {
        getCommand("mail").setExecutor(new CommandMail(this));
        getCommand("maildisable").setExecutor(new CommandMail(this));
        getCommand("deletemail").setExecutor(new CommandDeleteMail(this));
        getCommand("replymail").setExecutor(new CommandReplyMail());
    }

    @Override
    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new MailMenu(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMenu(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatEvents(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);
    }

    @Override
    public void registerTasks() {

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
        }else if(version.equals("v1_11_R1")){
            //server is running 1.11.* so we need to use the 1.11 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_11_R1();
        }else if(version.equals("v1_10_R1")){
            //server is running 1.10.* so we need to use the 1.10 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_10_R1();
        }else if(version.equals("v1_9_R1")){
            //server is running 1.9 - 1.9.2 so we need to use the 1.9 R1 NMSUtils class
            nmsUtils = new NMSUtils_1_9_R1();
        }else if(version.equals("v1_9_R2")){
            //server is running 1.9.4 so we need to use the 1.9 R2 NMSUtils class
            nmsUtils = new NMSUtils_1_9_R2();
        }
        // This will return true if the server version was compatible with one of our NMS classes
        // because if it is, our actionbar would not be null
        return nmsUtils != null;
    }

    public NMSUtils getNmsUtils() { return nmsUtils; }

    public FileConfiguration getStorageConfig() { return storageConfig.fileConfiguration; }
    public File getStorageConfigFile(){ return storageConfig.file; }

    /**
     * Disables a players mail. If already in the list it will remove player
     * @param player
     * @return True if added to list, False if removed from list
     */
    public boolean disableMail(Player player){
        if(mailDisabledPlayers.contains(player.getUniqueId())){
            mailDisabledPlayers.remove(player.getUniqueId());
            getServer().getScheduler().runTaskAsynchronously(this, this::saveMailDisabledPlayers);
            return false;
        }
        mailDisabledPlayers.add(player.getUniqueId());
        getServer().getScheduler().runTaskAsynchronously(this, this::saveMailDisabledPlayers);
        return true;
    }
    /**
     * Checks if player has mail disabled
     * @param player
     * @return
     */
    public boolean hasMailDisabled(UUID player){
        if(mailDisabledPlayers.contains(player))return true;
        return false;
    }

    private void loadMessages(){
        try{
            Set<String> uuids = getStorageConfig().getConfigurationSection("messages").getKeys(false);
            for(String uuidString: uuids){
                UUID uuid = UUID.fromString(uuidString);
                List<String> messages = getStorageConfig().getStringList("messages."+uuidString);
                messagesMap.put(uuid, new Messages(uuid, messages));
            }
        }catch (NullPointerException ex){
            Log.info("No mail to load");
        }
    }

    public void saveMessages(){
        for(UUID uuid:messagesMap.keySet()){
            Messages messages = messagesMap.get(uuid);
            List<String> messagesList = messages.getMessages();
            getStorageConfig().set("messages."+uuid.toString(), messagesList);
        }
        try{
            getStorageConfig().save(getStorageConfigFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadMailDisabledPlayers(){
        List<String> uuids = getStorageConfig().getStringList("mailDisabled");
        for(String uuidString: uuids){
            UUID uuid = UUID.fromString(uuidString);
            mailDisabledPlayers.add(uuid);
        }
    }

    public void saveMailDisabledPlayers(){
        List<String> uuids = new ArrayList<>();
        for(UUID uuid: mailDisabledPlayers){
            uuids.add(uuid.toString());
        }
        getStorageConfig().set("mailDisabled", uuids);
        try{
            getStorageConfig().save(getStorageConfigFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
