package ua.comparus.userservice.model;

public record UserSearchParams(String username, String name, String surname) {

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