package ua.comparus.userservice.repository;

import ua.comparus.userservice.model.UserDTO;
import ua.comparus.userservice.model.UserSearchParams;
import java.util.List;

public interface UserRepository {
    List<UserDTO> findAll(UserSearchParams params);
}