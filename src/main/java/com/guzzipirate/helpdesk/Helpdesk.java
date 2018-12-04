package com.guzzipirate.helpdesk;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class Helpdesk extends JavaPlugin {
    public DbConnector connector;
    public ITicketHandler ticketHandler;
    public FileConfiguration config;

    final Logger LOGGER = this.getLogger();

    @Override
    public void onEnable() {
        this.loadConfig();

        if (this.config.getBoolean("database.enabled")) {
            try {
                this.connector = new HelpdeskDbConnector(this.config.getString("database.host"),
                                                         this.config.getString("database.database"),
                                                         this.config.getString("database.user"),
                                                         this.config.getString("database.password"),
                                                         this.config.getString("database.prefix"));
                this.connector.LOGGER = this.LOGGER;
                this.connector.initialize();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            if (this.connector != null) {
                this.LOGGER.info(String.format("Successfully connected to MySQL DB with user %s", this.connector.username));
            }
            else {
                Bukkit.getPluginManager().disablePlugin(this);
            }

            // Generate a new SqlTicketHandler, because we're using the local database
            this.ticketHandler = new SqlTicketHandler(this.connector);
        }
        else {
            //this.ticketHandler = new WebTicketHandler(); TODO Implement web ticketing system
        }

        CommandSupport mgr = new CommandSupport(new SqlTicketHandler(this.connector));
        PluginCommand cmd = this.getCommand("support");
        cmd.setExecutor(mgr);
        cmd.setTabCompleter(mgr);
    }

    @Override
    public void onDisable() {
        if (this.connector != null) {
            this.connector.disconnect();
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.config = this.getConfig();
    }

    private void loadConfig() {
        this.saveDefaultConfig();
        this.config = this.getConfig();
        this.config.addDefault("database.enabled", false);
        this.config.addDefault("database.host", "localhost");
        this.config.addDefault("database.database", "minecraft");
        this.config.addDefault("database.user", "");
        this.config.addDefault("database.password", "");
        this.config.addDefault("database.prefix", "hd_");

        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
}
