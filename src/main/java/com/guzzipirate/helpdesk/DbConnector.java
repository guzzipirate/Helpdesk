package com.guzzipirate.helpdesk;

import javax.xml.transform.Result;
import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DbConnector {
    final String username;
    final String password;
    final String url;
    final String prefix;

    public static Connection connection;

    public Logger LOGGER;

    protected List<Table> tables;

    public DbConnector(String host, String db, String user, String pwd, String pref) {
        if (host == null) {
            throw new IllegalArgumentException("host");
        }
        if (db == null) {
            throw new IllegalArgumentException("db");
        }
        if (user == null) {
            throw new IllegalArgumentException("user");
        }
        if (pwd == null) {
            throw new IllegalArgumentException("pwd");
        }

        // Set the connection settings
        this.username = user;
        this.password = pwd;
        this.url = String.format("jdbc:mysql://%s:3306/%s", host, db);
        //this.url = jdbc:mysql://db4free.net:3306/DataBaseName;

        // prefix might be empty
        if (pref != null) {
            this.prefix = pref;
        }
        else {
            this.prefix = "";
        }
    }

    public void initialize() {
        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try { //Another try catch to get any SQL errors (for example connections errors)
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) { //catching errors)
            e.printStackTrace(); //prints out SQLException errors to the console (if any)
            return;
        }

        this.createDefaultTables();
    }

    public void disconnect() {
        // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (this.connection != null && !this.connection.isClosed()){ //checking if connection isn't null to
                //avoid receiving a nullpointer
                this.connection.close(); //closing the connection field variable.
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getTableName(String table) {
        return this.prefix + table;
    }

    public int insert(String query, List<Object> args) {
        PreparedStatement stmt = this.getStatement(query, args);
        ResultSet keys = null;

        // Statement ready, execute it now and fetch the generated key
        try {
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getDbIdentifier(String table, String column, String value, boolean genIfNotExists) {
        StringBuilder query = new StringBuilder("select ");
        query.append(table).append("_id");
        query.append(" from ");
        query.append(this.getTableName(table));
        query.append(" where ");
        query.append(column);
        query.append(" = ?;");

        List<Object> params = new ArrayList<Object>();
        params.add(value);

        // Try if we can fetch the id for this value
        int res = this.fetchInt(query.toString(), params);

        // If no result has been found and we are allowed to create it, do that
        if (res == -1 && genIfNotExists == true) {
            // ID doesn't seem to exist, create it
            query = new StringBuilder("insert into ");
            query.append(this.getTableName(table));
            query.append("(");
            query.append(column);
            query.append(") values (?);");

            res =  this.insert(query.toString(), params);
        }

        return res;
    }

    public int fetchInt(String sql, List<Object> params) {
        PreparedStatement stmt = this.getStatement(sql, params);
        ResultSet set;

        // Statement ready for execution
        try {
            set = stmt.executeQuery();
            if (set.next()) {
                return set.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return -1;
    }

    protected void createDefaultTables() {
        if (this.tables == null) {
            this.tables = new ArrayList<Table>();
        }
    }

    protected PreparedStatement getStatement(String query, List<Object> args) {
        PreparedStatement stmt = null;

        this.LOGGER.info("Will now execute sql >" + query.toString() + "<");

        try {
            stmt = this.connection.prepareStatement(query, 1);

            for (int idx = 0; idx < args.size(); idx++) {
                Object o = args.get(idx);
                if (o instanceof String) {
                    stmt.setString(idx + 1, (String)o);
                }
                else if (o instanceof Integer) {
                    stmt.setInt(idx + 1, (int)o);
                }
                else {
                    this.LOGGER.info("Object type not supported!");
                    return null;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return stmt;
    }
}
