package com.tyrellplayz.servermail.configs;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Creates/Loads the main config file of the plugin
 */
public class LanguageConfig {

    /* DEBUG */
    private boolean resetOnEnabled = false;

    private ServerMail sm;
    private FileConfiguration configuration = new YamlConfiguration();

    public LanguageConfig(ServerMail sm) {
        this.sm = sm;
        load();
    }

    private void load(){
        if(!sm.getDataFolder().exists())sm.getDataFolder().mkdirs();
        File file = new File(sm.getDataFolder(), "language.yml");
        if(file.exists()&&resetOnEnabled)file.delete();
        if(!file.exists())sm.saveResource("language.yml",false);
        try {
            configuration.load(file);
        } catch (Exception e) {
            LogMessagesUtil.errorMessage("Error while enabling","Config","Could not load 'language.yml'");
            Bukkit.getServer().getPluginManager().disablePlugin(sm);
            e.printStackTrace();
            return;
        }
        reloadConfig();
    }

    public void reloadConfig(){
        mailMenuTitle = configuration.getString("mailMenuTitle");
        readMessageText = configuration.getString("readMessageText");
        unReadMessageText = configuration.getString("unReadMessageText");
        informationText = configuration.getString("informationText");
        totalMailText = configuration.getString("totalMailText");
        sendMailText = configuration.getString("sendMailText");
        sendMailLoadText = configuration.getString("sendMailLoadText");
        previousPageText = configuration.getString("previousPageText");
        nextPageText = configuration.getString("nextPageText");
        sortNext = configuration.getString("sortNext");
        sortLatestMessagesText = configuration.getString("sortLatestMessagesText");
        sortOldMessagesText = configuration.getString("sortOldMessagesText");
        sortClickText = configuration.getString("sortClickText");
        exitText = configuration.getString("exitText");
        playerMenuTitle = configuration.getString("playerMenuTitle");
        playerHeadLoreText = configuration.getString("playerHeadLoreText");
        enterNameText = configuration.getString("enterNameText");
        backText = configuration.getString("backText");
        typeMessageText = configuration.getString("typeMessageText");
        cancelText = configuration.getString("cancelText");
        nameText = configuration.getString("nameText");
        sentByText = configuration.getString("sentByText");
        deleteText = configuration.getString("deleteText");
        replyText = configuration.getString("replyText");
        canceledText = configuration.getString("canceledText");
        mustBePlayerText = configuration.getString("mustBePlayerText");
        permissionMessage = configuration.getString("permissionMessage");
        messageDeletedText = configuration.getString("messageDeletedText");
        openingMailText = configuration.getString("openingMailText");
        disableMailText = configuration.getString("disableMailText");
        enableMailText = configuration.getString("enableMailText");
        cantSendNothingText = configuration.getString("cantSendNothingText");
        unableToFindPlayerText = configuration.getString("unableToFindPlayerText");
        playerDisabledMailText = configuration.getString("playerDisabledMailText");
        mailSentText = configuration.getString("mailSentText");
        // 0.2.0
        playerNeverOnline = configuration.getString("playerNeverOnline");
        mailSendMenuTitle = configuration.getString("mailSendMenuTitle");
        typeItemText = configuration.getString("typeItemText");
        typeMoneyText = configuration.getString("typeMoneyText");
        notEnoughMoney = configuration.getString("notEnoughMoney");
        typeMoneyMessageText = configuration.getString("typeMoneyMessageText");
        mustBeNumberText = configuration.getString("mustBeNumberText");
        receiveText = configuration.getString("receiveText");
        sendMessageMessage = configuration.getString("sendMessageMessage");
        sendItemMessage = configuration.getString("sendItemMessage");
        sendMoneyMessage = configuration.getString("sendMoneyMessage");
        itemReceivedText = configuration.getString("itemReceivedText");
        moneyReceivedText = configuration.getString("moneyReceivedText");
        nothingToReceive = configuration.getString("nothingToReceive");
        // 0.2.1
        newMailText = configuration.getString("newMailText");
    }

    private static String mailMenuTitle;
    private static String readMessageText;
    private static String unReadMessageText;
    private static String informationText;
    private static String totalMailText;
    private static String sendMailText;
    private static String sendMailLoadText;
    private static String previousPageText;
    private static String nextPageText;
    private static String sortNext;
    private static String sortLatestMessagesText;
    private static String sortOldMessagesText;
    private static String sortClickText;
    private static String exitText;
    private static String playerMenuTitle;
    private static String playerHeadLoreText;
    private static String enterNameText;
    private static String backText;
    private static String typeMessageText;
    private static String cancelText;
    private static String nameText;
    private static String sentByText;
    private static String deleteText;
    private static String replyText;
    private static String canceledText;
    private static String mustBePlayerText;
    private static String permissionMessage;
    private static String messageDeletedText;
    private static String openingMailText;
    private static String disableMailText;
    private static String enableMailText;
    private static String cantSendNothingText;
    private static String unableToFindPlayerText;
    private static String playerDisabledMailText;
    private static String mailSentText;
    // 0.2.0
    private static String mailSendMenuTitle;
    private static String playerNeverOnline;
    private static String typeItemText;
    private static String typeMoneyText;
    private static String notEnoughMoney;
    private static String typeMoneyMessageText;
    private static String mustBeNumberText;
    private static String receiveText;
    private static String sendMessageMessage;
    private static String sendItemMessage;
    private static String sendMoneyMessage;
    private static String itemReceivedText;
    private static String moneyReceivedText;
    private static String nothingToReceive;
    // 0.2.1
    private static String newMailText;

    public static String getMailMenuTitle() {
        return mailMenuTitle;
    }

    public static String getReadMessageText() {
        return readMessageText;
    }

    public static String getUnReadMessageText() {
        return unReadMessageText;
    }

    public static String getInformationText() {
        return informationText;
    }

    public static String getTotalMailText() {
        return totalMailText;
    }

    public static String getSendMailText() {
        return sendMailText;
    }

    public static String getSendMailLoadText() {
        return sendMailLoadText;
    }

    public static String getPreviousPageText() {
        return previousPageText;
    }

    public static String getNextPageText() {
        return nextPageText;
    }

    public static String getSortNext() {
        return sortNext;
    }

    public static String getSortLatestMessagesText() {
        return sortLatestMessagesText;
    }

    public static String getSortOldMessagesText() {
        return sortOldMessagesText;
    }

    public static String getSortClickText() {
        return sortClickText;
    }

    public static String getExitText() {
        return exitText;
    }

    public static String getPlayerMenuTitle() {
        return playerMenuTitle;
    }

    public static String getPlayerHeadLoreText() {
        return playerHeadLoreText;
    }

    public static String getEnterNameText() {
        return enterNameText;
    }

    public static String getBackText() {
        return backText;
    }

    public static String getTypeMessageText() {
        return typeMessageText;
    }

    public static String getCancelText() {
        return cancelText;
    }

    public static String getNameText() {
        return nameText;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public static String getSentByText() {
        return sentByText;
    }

    public static String getDeleteText() {
        return deleteText;
    }

    public static String getReplyText() {
        return replyText;
    }

    public static String getCanceledText() {
        return canceledText;
    }

    public static String getMustBePlayerText() {
        return mustBePlayerText;
    }

    public static String getPermissionMessage() {
        return permissionMessage;
    }

    public static String getMessageDeletedText() {
        return messageDeletedText;
    }

    public static String getOpeningMailText() {
        return openingMailText;
    }

    public static String getDisableMailText() {
        return disableMailText;
    }

    public static String getEnableMailText() {
        return enableMailText;
    }
    public static String getCantSendNothingText() {
        return cantSendNothingText;
    }

    public static String getUnableToFindPlayerText() {
        return unableToFindPlayerText;
    }

    public static String getPlayerDisabledMailText() {
        return playerDisabledMailText;
    }

    public static String getMailSentText() {
        return mailSentText;
    }

    public static String getPlayerNeverOnline() {
        return playerNeverOnline;
    }

    public static String getMailSendMenuTitle() { return mailSendMenuTitle; }

    public static String getTypeItemText() {
        return typeItemText;
    }

    public static String getTypeMoneyText() {
        return typeMoneyText;
    }

    public static String getNotEnoughMoney() {
        return notEnoughMoney;
    }

    public static String getTypeMoneyMessageText() {
        return typeMoneyMessageText;
    }

    public static String getMustBeNumberText() {
        return mustBeNumberText;
    }

    public static String getReceiveText() {
        return receiveText;
    }

    public static String getSendMessageMessage() {
        return sendMessageMessage;
    }

    public static String getSendItemMessage() {
        return sendItemMessage;
    }

    public static String getSendMoneyMessage() {
        return sendMoneyMessage;
    }

    public static String getItemReceivedText() {
        return itemReceivedText;
    }

    public static String getMoneyReceivedText() {
        return moneyReceivedText;
    }

    public static String getNothingToReceive() {
        return nothingToReceive;
    }

    public static String getNewMailText() {
        return newMailText;
    }
}
