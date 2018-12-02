package com.guzzipirate.helpdesk;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CommandSupport implements CommandExecutor, TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));

        List<String> cats = new ArrayList<String>();
        cats.add("report");
        cats.add("request");
        cats.add("register");

        // Still have to type out the main category
        if (arguments.size() <= 1) {
            // Nothing typed yet
            if (arguments.size() == 0 || arguments.get(0).equals("")) {
                return cats; //.stream().collect(Collectors.toList());
            }

            // Started to type
            return cats.stream().filter(s -> s.startsWith(arguments.get(0).toLowerCase())).collect(Collectors.toList());
        } // Report has sub categories
        else if (arguments.size() == 2 && arguments.get(0).equals("report")) {
            cats = new ArrayList<String>();
            cats.add("grief");
            cats.add("fire");
            cats.add("explosion");

            // Nothing typed yet
            if (arguments.get(1).equals("")) {
                return cats;
            }

            // Started to type
            return cats.stream().filter(s -> s.startsWith(arguments.get(1).toLowerCase())).collect(Collectors.toList());
        }

        return new ArrayList<String>();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        else if (args[0].equals("request") && args.length < 2) {
            sender.sendMessage("Please add a description to your request!");
            return true;
        }
        else if (args[0].equals("report") && args.length < 3) {
            sender.sendMessage("A report has to follow this format: /support report <type> <description>");
            return true;
        }

        Set<String> params = new HashSet<String>();
        params.add("report");
        params.add("register");
        params.add("request");

        if (params.contains(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                String subCat = "";
                String playerText = "";

                if (args[0].equals("report")) {
                    subCat = args[1];

                    playerText = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                }
                else if (args.length > 1) {
                    playerText = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                }

                int ticketId = createSupportTicket(args[0], subCat, playerText, player);
                player.sendMessage("Support ticket No. " + ticketId + " has been created for you. We'll get to you soon!");
                return true;
            }
            else {
                sender.sendMessage("This command can only be executed by a player!");
                return true;
            }
        }

        return false;
    }

    private int createSupportTicket(String cat, String subCat, String playerText, Player pl) {
        pl.sendMessage("I'd create support ticket with cat: " + cat + ", subcat: " + subCat + ", playerText: " + playerText);
        return 1;
    }
}
