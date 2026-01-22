package ua.comparus.userservice.model;

public record UserSearchParams(String username, String name, String surname) {
    public UserSearchParams {
        username = username != null ? username.trim() : null;
        name = name != null ? name.trim() : null;
        surname = surname != null ? surname.trim() : null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[ ");
        if (username != null && !username.isBlank()) {
            builder.append("username='").append(username).append("' ");
        }
        if (name != null && !name.isBlank()) {
            builder.append("name='").append(name).append("' ");
        }
        if (surname != null && !surname.isBlank()) {
            builder.append("surname='").append(surname).append("' ");
        }
        builder.append("]");
        return builder.toString().trim();
    }
}