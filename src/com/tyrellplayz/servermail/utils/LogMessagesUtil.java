package com.tyrellplayz.servermail.utils;

import com.tyrellplayz.servermail.ServerMail;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

public class LogMessagesUtil {

    public static void enableMessage(){
        logo();
        Log.info("<"+ServerMail.pdf.getVersion()+">");
        Log.info("-===================================================================-");
    }


    public static void errorMessage(String error, String section, String cause){
        errorLogo();
        Log.info("ServerMail <"+ServerMail.pdf.getVersion()+"> has encountered an error");
        Log.info("-===================================-");
        Log.info("Error: "+error);
        Log.info("Section: "+section);
        Log.info("Cause: "+cause);
        Log.info("Plugin is now disabled");
        Log.info("-===================================-");
    }


    public static void updateMessage(){
        updateLogo();
        Log.info("<"+ServerMail.pdf.getVersion()+">");
        Log.info("-===================================================================-");
    }

    public static void logo(){
        Log.info("  ____                                        __  __           _   _ ");
        Log.info(" / ___|    ___   _ __  __   __   ___   _ __  |  \\/  |   __ _  (_) | |");
        Log.info(" \\___ \\   / _ \\ | '__| \\ \\ / /  / _ \\ | '__| | |\\/| |  / _` | | | | |");
        Log.info("  ___) | |  __/ | |     \\ V /  |  __/ | |    | |  | | | (_| | | | | |");
        Log.info(" |____/   \\___| |_|      \\_/    \\___| |_|    |_|  |_|  \\__,_| |_| |_|");
    }

    public static void errorLogo(){
        Log.info("  _____                              ");
        Log.info(" | ____|  _ __   _ __    ___    _ __ ");
        Log.info(" |  _|   | '__| | '__|  / _ \\  | '__|");
        Log.info(" | |___  | |    | |    | (_) | | |   ");
        Log.info(" |_____| |_|    |_|     \\___/  |_|   ");
    }

    public static void updateLogo(){
        Log.info("  _   _               _           _          ");
        Log.info(" | | | |  _ __     __| |   __ _  | |_    ___ ");
        Log.info(" | | | | | '_ \\   / _` |  / _` | | __|  / _ \\");
        Log.info(" | |_| | | |_) | | (_| | | (_| | | |_  |  __/");
        Log.info("  \\___/  | .__/   \\__,_|  \\__,_|  \\__|  \\___|");
        Log.info("         |_|                                 ");
    }

}
