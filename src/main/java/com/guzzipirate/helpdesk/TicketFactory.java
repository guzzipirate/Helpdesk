package com.guzzipirate.helpdesk;

import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketFactory {
    // This method creates a new ticket, ready to be sent into the TicketHandler
    // The used ticketId is 0 here, because we get the real ID from the DB later on
    // Ticket: The generated ticket
    public static Ticket createNew(Player creator, String cat, String subcat, String text) {
        UUID creatorId = creator.getUniqueId();

        Ticket ticket = new Ticket(0, creatorId, "created", cat, subcat, creator.getLocation(), new Timestamp(System.currentTimeMillis()));
        ticket.addChangeSet(text, creatorId, "created");

        return ticket;
    }
}
