package com.guzzipirate.helpdesk;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.bukkit.Location;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ticket {
    // The unique id of the assigned editor of the ticket
    public UUID editor;

    // The current ticket state
    private String state;

    // The unique db id of the ticket
    private int ticketId;

    // The unique id of the assigned owner (creator) of the ticket
    private UUID owner;

    // Ticket category
    private String cat;

    // Ticket subcategory
    private String subcat;

    // All ChangeSets for this ticket
    private List<TicketChangeset> changeSets;

    // The location where the player created the ticket
    private Location createdLoc;

    // The time when the ticket was created
    private Timestamp createdDate;

    // Initializes a new instance of the Ticket class
    public Ticket(int ticketId, UUID creator, String state, String cat, String subcat, Location creatorLocation, Timestamp created) {
        if (creator == null) {
            throw new IllegalArgumentException("creator");
        }
        if (state == null) {
            throw new IllegalArgumentException("state");
        }
        if (cat == null) {
            throw new IllegalArgumentException("cat");
        }
        if (creatorLocation == null) {
            throw new IllegalArgumentException("creatorLocation");
        }

        // Set the class parameters
        this.ticketId = ticketId;
        this.owner = creator;
        this.state = state;
        this.cat = cat;
        this.subcat = subcat;
        this.createdLoc = creatorLocation;
        this.createdDate = created;
    }

    public int getTicketId() {
        return this.ticketId;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public String getCategory() {
        return this.cat;
    }

    public String getState() {
        return this.state;
    }

    public String getSubCategory() {
        return this.subcat;
    }

    public List<TicketChangeset> getChangeSets() {
        return this.changeSets;
    }

    public Location getCreatedLocation() {
        return this.createdLoc;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    // Creates a new changeset and adds it to the collection
    public void addChangeSet(String text, UUID modifier, String newState) {
        this.addChangeSet(new TicketChangeset(this.ticketId, text, modifier, state));
    }

    // Add a collection of changesets to the ticket. TicketId must be set correctly!
    public void addChangeSets(List<TicketChangeset> sets) {
        if (this.changeSets == null) {
            this.changeSets = sets;
        }
        else {
            for (int idx = 0; idx < sets.size(); idx++) {
                this.addChangeSet(sets.get(idx));
            }
        }
    }

    // Used to update the ticketId after the initial insert. Only possible if the ticketId is still 0
    // boolean: If the id has been updated
    public boolean updateTicketId(int ticketId) {
        if (this.ticketId == 0) {
            this.ticketId = ticketId;

            // Now update all changesets to have a valid record
            if (this.changeSets != null) {
                for (int idx = 0; idx < this.changeSets.size(); idx++) {
                    this.changeSets.get(idx).updateTicketId(ticketId);
                }
            }
            return true;
        }
        return false;
    }

    // Adds the given TicketChangeset to the collection
    private void addChangeSet(TicketChangeset set) {
        if (this.changeSets == null) {
            this.changeSets = new ArrayList<TicketChangeset>();
        }
        this.changeSets.add(set);

        // Update the ticket state to the state of the new changeset
        if (!this.state.equals(set.getState())) {
            this.state = set.getState();
        }
    }
}
