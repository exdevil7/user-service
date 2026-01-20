package ua.comparus.userservice.service;

import org.springframework.stereotype.Service;
import ua.comparus.userservice.model.UserDTO;
import ua.comparus.userservice.model.UserSearchParams;
import ua.comparus.userservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDTO> fetchAllUsers(UserSearchParams params) {
        return repository.findAll(params);
    }
}