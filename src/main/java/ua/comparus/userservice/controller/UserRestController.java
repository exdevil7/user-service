package ua.comparus.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ua.comparus.userservice.api.UsersApi;
import ua.comparus.userservice.model.UserDTO;
import ua.comparus.userservice.model.UserSearchParams;
import ua.comparus.userservice.service.UserService;

import java.util.List;

@RestController
@Validated
public class UserRestController implements UsersApi {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUser(String username, String name, String surname) {
        var params = new UserSearchParams(username, name, surname);
        log.info("/GET users with params: {}", params);

        var users = userService.fetchAllUsers(params);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(users);
    }
}
