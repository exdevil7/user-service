package ua.comparus.userservice.repository.util;

import java.util.ArrayList;
import java.util.List;

public class SqlQueryBuilder {
    private final StringBuilder sql = new StringBuilder();
    private final List<Object> params = new ArrayList<>();
    private boolean hasWhere = false;
    private boolean hasField = false;

    public SqlQueryBuilder select() {
        sql.append("SELECT ");
        return this;
    }

    public SqlQueryBuilder fieldAs(String field, String alias) {
        if (hasField) {
            sql.append(", ");
        }
        sql.append(field).append(" AS ").append(alias);
        hasField = true;
        return this;
    }

    public SqlQueryBuilder from(String table) {
        sql.append(" FROM ").append(table);
        return this;
    }

    public SqlQueryBuilder where(String field, String value) {
        if (value == null || value.isBlank()) {
            return this;
        }
        appendWhereClause();
        sql.append(field).append(" = ?");
        params.add(value);
        return this;
    }

    public SqlQueryBuilder and(String field, String value) {
        return where(field, value);
    }

    public SqlQueryBuilder whereLike(String field, String value) {
        if (value == null || value.isBlank()) {
            return this;
        }
        appendWhereClause();
        sql.append("LOWER(").append(field).append(") LIKE ?");
        params.add(value.toLowerCase() + "%");
        return this;
    }

    public SqlQueryBuilder andLike(String field, String value) {
        return whereLike(field, value);
    }

    public SqlQueryBuilder orderBy(String orderBy) {
        sql.append(" ORDER BY ").append(orderBy);
        return this;
    }

    public SqlQueryBuilder limit(int limit, int offset) {
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        return this;
    }

    private void appendWhereClause() {
        if (!hasWhere) {
            sql.append(" WHERE ");
            hasWhere = true;
        } else {
            sql.append(" AND ");
        }
    }

    public String build() {
        return sql.toString();
    }

    public List<Object> getParams() {
        return params;
    }
}
