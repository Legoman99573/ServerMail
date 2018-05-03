package com.tyrellplayz.servermail.mysql;

import com.tyrellplayz.servermail.IPlayerData;
import com.tyrellplayz.servermail.InvalidItemStackException;
import com.tyrellplayz.servermail.Mail;
import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import com.tyrellplayz.servermail.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMailSQLData implements IPlayerData{

    private UUID uuid;
    private ServerMail sm;

    public PlayerMailSQLData(ServerMail sm, UUID uuid) {
        this.uuid = uuid;
        this.sm = sm;
        reload();
    }

    public UUID getUUID() {
        return null;
    }

    @Override
    public void load(){

    }

    /**
     * Handles saving the data
     */
    public void save(){
        try{
            Connection con = sm.mySQLHook.getConnection();
            Statement st = con.createStatement();

            // Save PlayerName
            try{
                st.executeUpdate("DELETE FROM `playerNames` WHERE uuid = "+uuid.toString());
            }catch (SQLException ex){
                LogMessagesUtil.warningMessage("Failed to load player data for " + Bukkit.getServer().getOfflinePlayer(uuid).getName(), "Player SQL Data", "User has never been generated before. Adding it in " + uuid.toString() + " :)");
            }
            st.executeUpdate("INSERT INTO `playerNames` (`uuid`,`name`) VALUES ('"+uuid.toString()+"','" + Bukkit.getServer().getOfflinePlayer(uuid).getName() + "');");

            // Save mailDisabled
            st.executeUpdate("DELETE FROM `mailDisabled` WHERE uuid = "+uuid.toString());
            if(mailDisabled){
                st.executeUpdate("INSERT INTO `mailDisabled` (`uuid`) VALUES ('"+uuid.toString()+"');");
            }

            st.executeUpdate(String.format("DELETE FROM `%s`", uuid.toString()));
            for(Mail mail:mailList){
                if(mail.hasItemStack()){
                    Statement s = con.createStatement();
                    st.executeUpdate("INSERT INTO `"+uuid.toString()+"` (`message`, `sender`, `read`, `item`, `itemReceived`, `money`, `moneyReceived`) VALUES ('"+mail.getMessage()+"', '"+mail.getMessageSender()+"', '"+Utils.booleanToInt(mail.isRead())+"', '"+Utils.itemStackToString(mail.getItemStack())+"', '"+Utils.booleanToInt(mail.isItemReceived())+"', '0', '0');");
                }else if(mail.hasMoney()){
                    Statement s = con.createStatement();
                    st.executeUpdate("INSERT INTO `"+uuid.toString()+"` (`message`, `sender`, `read`, `item`, `itemReceived`, `money`, `moneyReceived`) VALUES ('"+mail.getMessage()+"', '"+mail.getMessageSender()+"', '"+Utils.booleanToInt(mail.isRead())+"', '', '0', '"+mail.getMoney()+"', '"+Utils.booleanToInt(mail.isMoneyReceived())+"');");
                }else{
                    Statement s = con.createStatement();
                    st.executeUpdate("INSERT INTO `"+uuid.toString()+"` (`message`, `sender`, `read`, `item`, `itemReceived`, `money`, `moneyReceived`) VALUES ('"+mail.getMessage()+"', '"+mail.getMessageSender()+"', '"+Utils.booleanToInt(mail.isRead())+"', '', '0', '0', '0');");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void reload() {
        try{
            Connection con = sm.mySQLHook.getConnection();
            Statement st = con.createStatement();

            // Load name from database
            ResultSet rsPlayerNames = st.executeQuery("SELECT * FROM `playerNames`");
            boolean runLoop = true;
            while(rsPlayerNames.next() && runLoop){
                if(rsPlayerNames.getString("uuid").equalsIgnoreCase(uuid.toString())){
                    if(rsPlayerNames.getString("name").equals(Bukkit.getServer().getOfflinePlayer(uuid).getName())){
                        playerName = rsPlayerNames.getString("name");
                    }else{
                        playerName = Bukkit.getServer().getOfflinePlayer(uuid).getName();
                    }
                    runLoop = false;
                }
            }
            // Load disabledMail from database
            mailDisabled = false;
            ResultSet rsMailDisabled = st.executeQuery("SELECT * FROM `playerNames`");
            boolean runLoop2 = true;
            while(rsMailDisabled.next() && runLoop2){
                if(rsMailDisabled.getString("uuid").equalsIgnoreCase(uuid.toString())){
                    mailDisabled = true;
                    runLoop2 = false;
                }
            }
            // Load mail from database
            ResultSet rsMail = st.executeQuery(String.format("SELECT * FROM `%s`", uuid.toString()));
            List<Mail> mail = new ArrayList<>();
            while (rsMail.next()) {
                String message = rsMail.getString("message");
                String sender = rsMail.getString("sender");
                Boolean read = rsMail.getBoolean("read");
                String item = rsMail.getString("item");
                Boolean itemReceived = rsMail.getBoolean("itemReceived");
                Double money = rsMail.getDouble("money");
                Boolean moneyReceived = rsMail.getBoolean("moneyReceived");
                if(!item.isEmpty()){
                    try{
                        ItemStack itemStack = Utils.stringToItemStack(item);
                        mailList.add(new Mail(sender, message, read, itemStack, itemReceived));
                    }catch (InvalidItemStackException ex){
                        LogMessagesUtil.errorMessage("Error while loading player data for "+uuid.toString(), "Player data", ex.getMessage());
                        ex.printStackTrace();
                        Bukkit.getServer().getPluginManager().disablePlugin(sm);
                        return;
                    }
                }else if(money > 0){
                    mail.add(new Mail(sender, message, read));
                }else{
                    mail.add(new Mail(sender, message, read, money, moneyReceived));
                }
            }
            _setMailList(mail);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private List<Mail> mailList = new ArrayList<>();

    /**
     * Gets the {@link Mail} from player data
     * @return
     */
    public List<Mail> getMailList() { return mailList; }

    public void _setMailList(List<Mail> mail){
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

    private String playerName;

    /**
     * Gets the players name set in config
     * @return
     */
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        sm.getServer().getScheduler().runTaskAsynchronously(sm, this::save);
    }

    private boolean mailDisabled;
    public boolean isMailDisabled() {
        return mailDisabled;
    }
    public void setMailDisabled(boolean mailDisabled) {
        this.mailDisabled = mailDisabled;
        sm.getServer().getScheduler().runTaskAsynchronously(sm, this::save);
    }
}
