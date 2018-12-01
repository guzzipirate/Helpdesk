package io.github.guzzipirate.helpdesk;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("helpdesk").setExecutor(new CommandHelpdesk());
    }

     @Override
     public void onDisable() {
        getLogger().info("Disabling Helpdesk");
     }
}
