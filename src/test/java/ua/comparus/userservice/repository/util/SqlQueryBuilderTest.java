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
                .andLike("first_name", "test_name")
                .andLike("last_name", "test_surname");

        assertEquals(
                "SELECT user_id AS id, login AS username, first_name AS name, last_name AS surname FROM users WHERE login = ? AND first_name LIKE ? AND last_name LIKE ?",
                sqlBuilder.build());
        assertEquals(3, sqlBuilder.getParams().size());
        assertTrue(sqlBuilder.getParams().contains("test_username"));
        assertTrue(sqlBuilder.getParams().contains("test_name%"));
        assertTrue(sqlBuilder.getParams().contains("test_surname%"));
    }


}