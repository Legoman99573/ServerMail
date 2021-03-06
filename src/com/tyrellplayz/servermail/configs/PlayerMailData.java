package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.InvalidItemStackException;
import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import com.tyrellplayz.servermail.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerMailData implements IConfig{

    private UUID uuid;
    private ServerMail sm;
    private File file;
    private FileConfiguration configuration;

    public PlayerMailData(ServerMail sm, UUID uuid) {
        this.uuid = uuid;
        this.sm = sm;
        File folder = new File(sm.getDataFolder()+ File.separator+"players");
        if(!folder.exists())folder.mkdirs();
        this.file = new File(folder, uuid+".yml");
        this.configuration = new YamlConfiguration();
        load();
        reload();
    }

    @Override
    public String getFileLocation(){
        return file.getPath();
    }

    @Override
    public String getFileName(){
        return file.getName();
    }

    @Override
    public FileConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public void load() {
        try{
            if(!file.exists())file.createNewFile();
            configuration.load(file);
        }catch (Exception ex){
            LogMessagesUtil.errorMessage("Error while loading player data","Player data","Was unable to load data for "+uuid.toString());
            ex.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
        }
    }

    /**
     * Handles saving the data
     */
    public void save(){
        configuration.set("name",playerName);
        configuration.set("mail",null);
        try{
            configuration.save(file);
        }catch (Exception ex){
            Log.error("Was unable to save player data for "+uuid.toString());
            ex.printStackTrace();
        }

        for(Mail mail:mailList){
            String pathPrefix = "mail."+mail.getMessage()+".";
            configuration.set(pathPrefix+"sender",mail.getMessageSender());
            configuration.set(pathPrefix+"read",mail.isRead());
            if(mail.hasItemStack()){
                configuration.set(pathPrefix+"item",Utils.itemStackToString(mail.getItemStack()));
                configuration.set(pathPrefix+"itemReceived",mail.isItemReceived());
            }else if(mail.hasMoney()){
                configuration.set(pathPrefix+"money",mail.getMoney());
                configuration.set(pathPrefix+"moneyReceived",mail.isMoneyReceived());
            }
        }
        try{
            configuration.save(file);
        }catch (Exception ex){
            Log.error("Was unable to save player data for "+uuid.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void reload() {
        configuration.getString("");
        List<Mail> mailList = new ArrayList<>();
        // Gets all message from player data config
        if(configuration.getConfigurationSection("mail") !=null){
            Set<String> mail = configuration.getConfigurationSection("mail").getKeys(false);
            for(String message:mail){
                String sender = configuration.getString("mail."+message+".sender");
                boolean read = configuration.getBoolean("mail."+message+".read");
                String item = configuration.getString("mail."+message+".item");
                double money = configuration.getDouble("mail."+message+"money");
                if(item==null && money==0){
                    mailList.add(new Mail(sender, message, read));
                }else if(item!=null && money==0){
                    boolean itemReceived = configuration.getBoolean("mail."+message+"itemReceived");
                    try{
                        ItemStack itemStack = Utils.stringToItemStack(item);
                        mailList.add(new Mail(sender, message, read, itemStack, itemReceived));
                    }catch (InvalidItemStackException ex){
                        LogMessagesUtil.errorMessage("Error while loading player data for "+uuid.toString(), "Player data", ex.getMessage());
                        ex.printStackTrace();
                        Bukkit.getServer().getPluginManager().disablePlugin(sm);
                        return;
                    }
                }else if(item==null && money > 0){
                    boolean moneyReceived = configuration.getBoolean("mail."+message+"moneyReceived");
                    mailList.add(new Mail(sender, message, read, money, moneyReceived));
                }
            }
            _setMailList(mailList);
        }

    }

    private List<Mail> mailList = new ArrayList<>();

    /**
     * Gets the {@link Mail} from player data
     * @return
     */
    public List<Mail> getMailList() { return mailList; }

    private void _setMailList(List<Mail> mail){
        this.mailList = mail;
    }
    /**
     * Adds a {@link Mail} to player data. Saves automatically
     * @param mail - {@link Mail} to add
     * @return True if successful, False if not
     */
    public boolean addMail(Mail mail){
        this.mailList.add(mail);
        sm.getServer().getScheduler().runTaskAsynchronously(sm, this::save);
        return true;
    }

    /**
     * Removes a {@link Mail} from player data. Saves automatically
     * @param mail - {@link Mail} to remove
     * @return True if successful, False if not
     */
    public boolean removeMail(Mail mail){
        if(this.mailList.contains(mail)){
            this.mailList.remove(mail);
            sm.getServer().getScheduler().runTaskAsynchronously(sm, this::save);
            return true;
        }
        return false;
    }

    /**
     * Removes a {@link Mail} from player data. Saves automatically
     * @param index - Index of message in messages list
     * @return True if successful, False if not
     */
    public boolean removeMail(int index){
        try{
            this.mailList.remove(index);
            sm.getServer().getScheduler().runTaskAsynchronously(sm, this::save);
            return true;
        }catch (ArrayIndexOutOfBoundsException ex){
            return false;
        }
    }

    private static String playerName;
    public static String getPlayerName() {
        return playerName;
    }
}
