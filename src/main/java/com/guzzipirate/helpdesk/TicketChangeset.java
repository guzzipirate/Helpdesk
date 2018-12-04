package com.guzzipirate.helpdesk;

import org.bukkit.entity.Player;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class TicketChangeset {
    private int changesetId;

    private int ticketId;

    private String text;

    private UUID modifier;

    private String state;

    private Timestamp timestamp;

    public TicketChangeset(int id, String text, UUID modifier, String state) {
        this(id, text, modifier, state, null);
    }

    public TicketChangeset(int id, String text, UUID modifier, String state, Timestamp changedate) {
        if (modifier == null) {
            throw new IllegalArgumentException("modifier");
        }
        if (state == null) {
            throw new IllegalArgumentException("state");
        }

        this.ticketId = id;
        this.text = text;
        this.modifier = modifier;
        this.state = state;
        this.timestamp = changedate;
    }

    public int getTicketId() {
        return this.ticketId;
    }

    public String getText() {
        return this.text;
    }

    public UUID getModifier() {
        return this.modifier;
    }

    public String getState() {
        return this.state;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    // Used to update the ticketId after the initial insert. Only possible if the ticketId is still 0
    // boolean: If the id has been updated
    public boolean updateTicketId(int ticketId) {
        if (this.ticketId == 0) {
            this.ticketId = ticketId;
            return true;
        }
        return false;
    }
}
