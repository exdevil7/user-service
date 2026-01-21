package ua.comparus.userservice.repository.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlQueryBuilderTest {
    // temporary test to make sure the builder works as expected
    @Test
    void testBuildWithNoConditions() {
        var sqlBuilder = new SqlQueryBuilder()
                .select()
                .fieldAs("user_id", "id")
                .fieldAs("login", "username")
                .fieldAs("first_name", "name")
                .fieldAs("last_name", "surname")
                .from("users")
                .where("login", "test_username")
                .andLike("first_name", "Testname")
                .andLike("last_name", null);

        assertEquals(
                "SELECT user_id AS id, login AS username, first_name AS name, last_name AS surname FROM users WHERE login = ? AND LOWER(first_name) LIKE ?",
                sqlBuilder.build());
        assertEquals(2, sqlBuilder.getParams().size());
        assertTrue(sqlBuilder.getParams().contains("test_username"));
        assertTrue(sqlBuilder.getParams().contains("testname%"));
    }
}