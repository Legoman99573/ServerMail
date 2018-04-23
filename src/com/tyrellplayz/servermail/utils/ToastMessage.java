package com.tyrellplayz.servermail.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;

/**
 * Only works for 1.12
 */
public class ToastMessage {

    private NamespacedKey id;
    private String icon;
    private String title, description;
    private String frame = "goal";
    private boolean announce = false, toast = true;
    private JavaPlugin pl;

    /**
     * Create a Toast/Advancement display (Top right corner)
     * @param id A unique id for this Advancement, will be the name if advancement file
     * @param title Message to show/send
     * @param icon minecraft id of display item (minecraft:...)
     * @param pl Your plugin instance
     */
    public ToastMessage(String id, String title, String icon, JavaPlugin pl)	{
        this(new NamespacedKey(pl, id), title, icon, pl);
    }

    /**
     * Create a Toast/Advancement display (Top right corner)
     * @param id A unique id for this Advancement, will be the name if advancement file
     * @param title Message to show/send
     * @param icon minecraft id of display item (minecraft:...)
     * @param pl Your plugin instance
     */
    public ToastMessage(NamespacedKey id, String title, String icon, JavaPlugin pl) {
        this.id = id;
        this.title = title;
        this.description = "§7This Toast was created with DreamAPI";
        this.icon = icon;
        this.pl = pl;
    }

    public void showTo(Player player)	{
        showTo(Arrays.asList(player));
    }

    public void showTo(Collection<? extends Player> players)	{
        add();
        grant(players);
        pl.getServer().getScheduler().runTaskLater(pl, () -> {
            revoke(players);
            remove();
        },20);
    }

    @SuppressWarnings("deprecation")
    private void add()	{
        try {
            Bukkit.getUnsafe().loadAdvancement(id, getJson());
            //Bukkit.getLogger().info("Advancement " + id + " saved");
        } catch (IllegalArgumentException e){
            //Bukkit.getLogger().info("Error while saving, Advancement " + id + " seems to already exist");
        }
    }

    @SuppressWarnings("deprecation")
    private void remove()	{
        Bukkit.getUnsafe().removeAdvancement(id);
    }

    private void grant(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players)	{

            progress = player.getAdvancementProgress(advancement);
            if (!progress.isDone())	{
                for (String criteria : progress.getRemainingCriteria())	{
                    progress.awardCriteria(criteria);
                }
            }
        }
    }

    private void revoke(Collection<? extends Player> players)	{
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players)	{
            progress = player.getAdvancementProgress(advancement);
            if (progress.isDone())	{
                for (String criteria : progress.getAwardedCriteria())	{
                    progress.revokeCriteria(criteria);
                }
            }
        }
    }

    public String getJson()	{

        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.addProperty("title", this.title);
        display.addProperty("description", this.description);
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame);
        display.addProperty("announce_to_chat", announce);
        display.addProperty("show_toast", toast);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);

    }

}
