package com.tyrellplayz.servermail;

/**
 * Used to create default methods I always use
 */
public interface IPlugin{

    // Plugin methods
    void onLoad();
    void onEnable();
    void onDisable();

    // My methods
    void registerCommands();
    void registerEvents();
    void registerTasks();

}
