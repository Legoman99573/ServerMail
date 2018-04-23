package com.tyrellplayz.servermail.utils;

import com.tyrellplayz.servermail.ServerMail;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

public class LogMessagesUtil {

    public static void enableMessage(){
        logo();
        ServerMail.logger.info("<"+ServerMail.pdf.getVersion()+">");
        ServerMail.logger.info("-===================================================================-");
    }


    public static void errorMessage(String error, String section, String cause){
        errorLogo();
        ServerMail.logger.info("ServerMail <"+ServerMail.pdf.getVersion()+"> has encountered an error");
        ServerMail.logger.info("-===================================-");
        ServerMail.logger.info("Error: "+error);
        ServerMail.logger.info("Section: "+section);
        ServerMail.logger.info("Cause: "+cause);
        ServerMail.logger.info("Plugin is now disabled");
        ServerMail.logger.info("-===================================-");
    }

    public static void updateMessage(String newVersion, String url){
        updateLogo();
        ServerMail.logger.info("<"+ServerMail.pdf.getVersion()+">");
        ServerMail.logger.info("-===================================================================-");
        ServerMail.logger.info("New Version: "+newVersion);
        ServerMail.logger.info("Download here: "+url);
        ServerMail.logger.info("-===================================================================-");
    }

    private static void logo(){
        ServerMail.logger.info("  ____                                        __  __           _   _ ");
        ServerMail.logger.info(" / ___|    ___   _ __  __   __   ___   _ __  |  \\/  |   __ _  (_) | |");
        ServerMail.logger.info(" \\___ \\   / _ \\ | '__| \\ \\ / /  / _ \\ | '__| | |\\/| |  / _` | | | | |");
        ServerMail.logger.info("  ___) | |  __/ | |     \\ V /  |  __/ | |    | |  | | | (_| | | | | |");
        ServerMail.logger.info(" |____/   \\___| |_|      \\_/    \\___| |_|    |_|  |_|  \\__,_| |_| |_|");
    }

    private static void errorLogo(){
        ServerMail.logger.info("  _____                              ");
        ServerMail.logger.info(" | ____|  _ __   _ __    ___    _ __ ");
        ServerMail.logger.info(" |  _|   | '__| | '__|  / _ \\  | '__|");
        ServerMail.logger.info(" | |___  | |    | |    | (_) | | |   ");
        ServerMail.logger.info(" |_____| |_|    |_|     \\___/  |_|   ");
    }

    private static void updateLogo(){
        ServerMail.logger.info("  _   _               _           _          ");
        ServerMail.logger.info(" | | | |  _ __     __| |   __ _  | |_    ___ ");
        ServerMail.logger.info(" | | | | | '_ \\   / _` |  / _` | | __|  / _ \\");
        ServerMail.logger.info(" | |_| | | |_) | | (_| | | (_| | | |_  |  __/");
        ServerMail.logger.info("  \\___/  | .__/   \\__,_|  \\__,_|  \\__|  \\___|");
        ServerMail.logger.info("         |_|                                 ");
    }

}
