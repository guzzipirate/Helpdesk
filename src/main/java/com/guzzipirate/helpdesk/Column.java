package com.guzzipirate.helpdesk;

public class Column {
    private String name;

    private String type;

    private boolean autoIncrement;

    private boolean primary;

    private boolean unique;

    private boolean notNull;

    private Table table;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Get the name of the column
    public String getName() {
        return this.name;
    }

    // Get the type of the column
    public String getType() {
        return this.type;
    }

    // If the value should increment automatically
    public boolean doAutoIncrement() {
        return this.autoIncrement;
    }

    // If this column is the primary key
    public boolean isPrimary() {
        return this.primary;
    }

    // If the value of the column has to be unique
    public boolean isUnique() {
        return this.unique;
    }

    // If nulls are allowed in this column
    public boolean isNotNullColumn() {
        return this.notNull;
    }

    // Sets the table where the column is in
    public void setTable(Table table) {
        if (this.table == null) {
            this.table = table;
        }
    }

    // Method sets the different db options for a column
    public void setOptions(boolean primaryKey, boolean autoIncrement, boolean unique, boolean notNull) {
        this.primary = primaryKey;
        this.autoIncrement = autoIncrement;
        this.unique = unique;
        this.notNull = notNull;
    }
}
