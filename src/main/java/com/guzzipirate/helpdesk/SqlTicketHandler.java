package com.guzzipirate.helpdesk;

import java.util.ArrayList;
import java.util.List;

public class SqlTicketHandler implements ITicketHandler {
    private DbConnector connector;

    private List<Ticket> cachedTickets;

    public SqlTicketHandler(DbConnector conn) {
        if (conn == null) {
            throw new IllegalArgumentException("conn");
        }
        this.connector = conn;
    }

    @Override
    public Ticket createTicket(Ticket t) {
        if (t == null) {
            throw new IllegalArgumentException("t");
        }

        // Get the parameters for the insert statement
        List<Object> params = new ArrayList<Object>();

        // Get the ids for the values from the database
        params.add(this.connector.getDbIdentifier("category", "name", t.getCategory(), true));
        if (t.getSubCategory() != null) {
            params.add(this.connector.getDbIdentifier("category", "name", t.getSubCategory(), true));
        }
        params.add(this.connector.getDbIdentifier("user", "uuid", t.getOwner().toString(), true));
        params.add(this.connector.getDbIdentifier("ticketstate", "name", t.getState(), true));

        params.add(DbConverter.convertToDbLocation(t.getCreatedLocation()));

        // Build the sql query depending on the amount of parameters we have
        StringBuilder query = new StringBuilder("insert into ");
        query.append(this.connector.getTableName("ticket"));

        // 4 parameters means without subcategory
        if (params.size() == 4) {
            query.append("(category, creator_id, ticketstate, location) values (?, ?, ?, ?);");
        }
        else {
            query.append("(category, subcategory, creator_id, ticketstate, location) values (?, ?, ?, ?, ?);");
        }

        // Execute the statement. Return value is the newly generated ticketId
        int dbTicketId = this.connector.insert(query.toString(), params);
        t.updateTicketId(dbTicketId);

        this.saveChangeSets(t.getChangeSets());
        return t;
    }

    @Override
    public Ticket getTicket(int ticketId) {
        return null;
    }

    @Override
    public List<Ticket> getUserTickets(String uuid) {
        return null;
    }

    @Override
    public List<Ticket> getEditorTickets(String uuid) {
        return null;
    }

    @Override
    public int addChangeSet(TicketChangeset cs) {
        return 0;
    }

    @Override
    public int setEditor(int ticketId, String uuid) {
        return 0;
    }

    private void updateTicket(Ticket t) {
        // First update the main ticket object
        StringBuilder query = new StringBuilder("update ");
        query.append(this.connector.getTableName("ticket"));
        query.append(" set editor_id = ?, ticketstate = ? where ticket_id = ?;");

        List<Object> params = new ArrayList<Object>();
        params.add(this.connector.getDbIdentifier("user", "uuid", t.editor.toString(), true));
        params.add(this.connector.getDbIdentifier("ticketstate", "name", t.getState(), true));
        params.add(t.getTicketId());

        // Send the update to the db
        this.connector.insert(query.toString(), params);

        this.saveChangeSets(t.getChangeSets());
    }

    private void saveChangeSets(List<TicketChangeset> changesets) {
        if (changesets == null) {
            throw new IllegalArgumentException("changesets");
        }
        if (changesets.size() < 1) {
            throw new IllegalArgumentException("changesets doesn't contain any values");
        }

        // First get highest lfd number currently in the db
        StringBuilder query = new StringBuilder("select lfd from ");
        query.append(this.connector.getTableName("changeset"));
        query.append(" where ticket_id = ? order by lfd desc limit 1;");

        List<Object> params = new ArrayList<Object>();
        params.add(changesets.get(0).getTicketId());

        int maxLdf = this.connector.fetchInt(query.toString(), params);

        // Values smaller than 0 should just start the loop with idx 0
        if (maxLdf < 0) {
            maxLdf = 0;
        }

        // Now insert all new changesets into the database
        for (int idx = maxLdf; idx < changesets.size(); idx++) {
            this.saveChangeSet(changesets.get(idx), idx + 1);
        }
    }

    private void saveChangeSet(TicketChangeset changeset, int lfd) {
        if (changeset == null) {
            throw new IllegalArgumentException("changeset");
        }
        if (lfd == 0) {
            throw new IllegalArgumentException("ldf too small");
        }

        // Create the insert statement for the changeset
        StringBuilder query = new StringBuilder("insert into ");
        query.append(this.connector.getTableName("changeset"));
        query.append(" (ticket_id, lfd, modifier_id, text, ticketstate) values (?, ?, ?, ?, ?);");

        List<Object> params = new ArrayList<Object>();
        params.add(changeset.getTicketId());
        params.add(lfd);
        params.add(this.connector.getDbIdentifier("user", "uuid", changeset.getModifier().toString(), true));
        params.add(changeset.getText());
        params.add(this.connector.getDbIdentifier("ticketstate", "name", changeset.getState(), true));

        // Now insert the changeset into the database
        this.connector.insert(query.toString(), params);
    }
}
