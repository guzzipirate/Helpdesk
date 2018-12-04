package com.guzzipirate.helpdesk;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelpdeskDbConnector extends DbConnector {
    public HelpdeskDbConnector(String host, String db, String user, String pwd, String pref) {
        super(host, db, user, pwd, pref);
    }

    @Override
    protected void createDefaultTables() {
        super.createDefaultTables();

        // User table
        Table tab = new Table("user");

        Column col = new Column("user_id", "INT(10)");
        col.setOptions(true, true, true, true);
        tab.addColumn(col);

        //col = new Column("name", "VARCHAR(100)");
        //col.setOptions(false, false, true, true);
        //tab.addColumn(col);

        col = new Column("uuid", "VARCHAR(100)");
        col.setOptions(false, false, true, true);
        tab.addColumn(col);

        this.tables.add(tab);

        // Ticketstate table
        tab = new Table("ticketstate");

        col = new Column("ticketstate_id", "INT(10)");
        col.setOptions(true, true, true, true);
        tab.addColumn(col);

        col = new Column("name", "VARCHAR(50)");
        col.setOptions(false, false, true, true);
        tab.addColumn(col);

        this.tables.add(tab);

        // Category table
        tab = new Table("category");

        col = new Column("category_id", "INT(10)");
        col.setOptions(true, true, true, true);
        tab.addColumn(col);

        col = new Column("parent", "INT(10)");
        tab.addColumn(col);
        tab.addConstraint("foreign key (parent) references " + this.getTableName("category") + "(category_id)");

        col = new Column("name", "VARCHAR(50)");
        col.setOptions(false, false, true, true);
        tab.addColumn(col);

        col = new Column("description", "VARCHAR(200)");
        tab.addColumn(col);

        this.tables.add(tab);

        // Ticket table
        tab = new Table("ticket");

        col = new Column("ticket_id", "INT(10)");
        col.setOptions(true, true, true, true);
        tab.addColumn(col);

        col = new Column("category", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (category) references " + this.getTableName("category") + "(category_id)");

        col = new Column("subcategory", "INT(10)");
        tab.addColumn(col);
        tab.addConstraint("foreign key (subcategory) references " + this.getTableName("category") + "(category_id)");

        col = new Column("creator_id", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (creator_id) references " + this.getTableName("user") + "(user_id)");

        col = new Column("editor_id", "INT(10)");
        //col.setOptions(false, false, false, false);
        tab.addColumn(col);
        tab.addConstraint("foreign key (editor_id) references " + this.getTableName("user") + "(user_id)");

        col = new Column("ticketstate", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (ticketstate) references " + this.getTableName("ticketstate") + "(ticketstate_id)");

        col = new Column("location", "VARCHAR(200)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);

        col = new Column("created", "TIMESTAMP");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);

        this.tables.add(tab);

        // Changeset table
        tab = new Table("changeset");

        col = new Column("changeset_id", "INT(10)");
        col.setOptions(true, true, true, true);
        tab.addColumn(col);

        col = new Column("ticket_id", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (ticket_id) references " + this.getTableName("ticket") + "(ticket_id)");

        col = new Column("lfd", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);

        col = new Column("modifier_id", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (modifier_id) references " + this.getTableName("user") + "(user_id)");

        col = new Column("text", "VARCHAR(1000)");
        tab.addColumn(col);

        col = new Column("ticketstate", "INT(10)");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);
        tab.addConstraint("foreign key (ticketstate) references " + this.getTableName("ticketstate") + "(ticketstate_id)");

        col = new Column("created", "TIMESTAMP");
        col.setOptions(false, false, false, true);
        tab.addColumn(col);

        this.tables.add(tab);

        PreparedStatement stmt = null;
        for (int idx = 0; idx < this.tables.size(); idx++) {
            try {
                stmt = this.connection.prepareStatement(this.tables.get(idx).getCreateStatement(this.prefix));
                stmt.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    }
                    catch (SQLException e) {
                    }
                }
            }
        }

        if (this.LOGGER != null) {
            this.LOGGER.info("Created default tables!");
        }
    }
}