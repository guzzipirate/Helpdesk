package com.guzzipirate.helpdesk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Helpdesk extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandSupport mgr = new CommandSupport();
        PluginCommand cmd = this.getCommand("support");
        cmd.setExecutor(mgr);
        cmd.setTabCompleter(mgr);
        //this.getCommand("support").setExecutor(new CommandSupport());
    }

     @Override
     public void onDisable() {
        getLogger().info("Disabling Helpdesk");
     }
}
