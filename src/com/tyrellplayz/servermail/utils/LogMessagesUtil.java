package com.tyrellplayz.servermail.utils;

import com.tyrellplayz.servermail.ServerMail;

public class LogMessagesUtil {

    public static void enableMessage(){
        ServerMail.logger.info("<"+ServerMail.pdf.getVersion()+">");
        ServerMail.logger.info("-===================================================================-");
    }


    public static void errorMessage(String error, String section, String cause) {
        ServerMail.logger.severe("ServerMail <"+ServerMail.pdf.getVersion()+"> has encountered an error");
        ServerMail.logger.severe("-===================================-");
        ServerMail.logger.severe("Error: " + error);
        ServerMail.logger.severe("Section: " + section);
        ServerMail.logger.severe("Cause: " + cause);
        ServerMail.logger.severe("Plugin may not function properly");
        ServerMail.logger.severe("-===================================-");
    }

    public static void warningMessage(String warning, String section, String cause) {
        ServerMail.logger.warning("ServerMail <"+ServerMail.pdf.getVersion()+"> has encountered a warning");
        ServerMail.logger.warning("-===================================-");
        ServerMail.logger.warning("Warning: " + warning);
        ServerMail.logger.warning("Section: " + section);
        ServerMail.logger.warning("Cause: " + cause);
        ServerMail.logger.warning("This does no harm to the plugin");
        ServerMail.logger.warning("-===================================-");
    }

    public static void updateMessage(String newVersion, String url) {
        ServerMail.logger.info("<"+ServerMail.pdf.getVersion()+">");
        ServerMail.logger.info("-===================================================================-");
        ServerMail.logger.info("New Version: "+newVersion);
        ServerMail.logger.info("Download here: "+url);
        ServerMail.logger.info("-===================================================================-");
    }
}
