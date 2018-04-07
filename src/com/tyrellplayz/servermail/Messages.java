package com.tyrellplayz.servermail;

import java.util.List;
import java.util.UUID;

public class Messages {

    /*
    NOTE: Message format - 'senderName,timeSent,message'
     */

    private List<String> messages;
    private UUID playerUUID;

    public Messages(UUID playerUUID, List<String> messages) {
        this.messages = messages;
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public void removeMessage(String message){
        messages.remove(message);
    }

}
