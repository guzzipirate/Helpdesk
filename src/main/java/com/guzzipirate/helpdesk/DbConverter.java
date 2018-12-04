package com.guzzipirate.helpdesk;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import java.util.UUID;

public class DbConverter {
    // Generate a semicolon separated string to store a location in the database
    // String: The generated string
    public static String convertToDbLocation(Location loc) {
        StringBuilder locstring = new StringBuilder();
        locstring.append(loc.getWorld().getUID());
        locstring.append(";");
        locstring.append(loc.getBlockX());
        locstring.append(";");
        locstring.append(loc.getBlockY());
        locstring.append(";");
        locstring.append(loc.getBlockZ());
        return locstring.toString();
    }

    // Generate a location out of the given db string. Uses the server to load the correct world
    // Location: The corresponding location
    public static Location convertToBukkitLocation(String dbLoc, Server server) {
        String[] parts = dbLoc.split(";");

        if (parts.length != 4) {
            throw new IllegalArgumentException("dbLoc string is in wrong format!");
        }

        // Generate a location out of the given db location string
        return new Location(server.getWorld(UUID.fromString(parts[0])),
                            Float.parseFloat(parts[1]),
                            Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]));
    }
}
