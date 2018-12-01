package com.guzzipirate.helpdesk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Helpdesk extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("support").setExecutor(new CommandSupport());
    }

     @Override
     public void onDisable() {
        getLogger().info("Disabling Helpdesk");
     }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return super.onTabComplete(sender, command, alias, args);
    }
}
