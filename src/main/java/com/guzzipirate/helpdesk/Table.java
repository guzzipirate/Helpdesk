package com.guzzipirate.helpdesk;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;

    private List<Column> columns;

    private List<String> constraints;

    public Table(String name) {
        this.name = name;
    }

    public void addColumn(Column col) {
        if (this.columns == null) {
            this.columns = new ArrayList<Column>();
        }

        col.setTable(this);
        this.columns.add(col);
    }

    public void addConstraint(String constr) {
        if (this.constraints == null) {
            this.constraints = new ArrayList<String>();
        }

        this.constraints.add(constr);
    }

    public String getCreateStatement(String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        StringBuilder sql = new StringBuilder("create table if not exists ");
        sql.append(prefix).append(this.name);
        sql.append("(");

        for (int idx = 0; idx < this.columns.size(); idx++) {
            Column col = this.columns.get(idx);

            sql.append(col.getName());
            sql.append(" ");
            sql.append(col.getType());
            sql.append(" ");

            if (col.isPrimary()) {
                sql.append("PRIMARY KEY ");
            }
            if (col.doAutoIncrement()) {
                sql.append("AUTO_INCREMENT ");
            }
            if (col.isUnique()) {
                sql.append("UNIQUE ");
            }
            if (col.isNotNullColumn()) {
                sql.append("NOT NULL ");
            }

            // still more columns to add
            if (idx < this.columns.size() - 1) {
                sql.append(", ");
            }
        }

        if (this.constraints != null) {
            for (int idx = 0; idx < this.constraints.size(); idx++) {
                sql.append(", ");
                sql.append(this.constraints.get(idx));
            }
        }

        sql.append(");");
        return sql.toString();
    }
}
