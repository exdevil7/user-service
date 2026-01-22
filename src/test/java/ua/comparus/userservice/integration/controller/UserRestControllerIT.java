package ua.comparus.userservice.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;
import ua.comparus.userservice.integration.TestcontainersIT;
import ua.comparus.userservice.model.UserDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerIT extends TestcontainersIT {

    private static final ParameterizedTypeReference<List<UserDTO>> USERS_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    @Autowired
    private RestTestClient restClient;

    @Test
    void shouldReturnUsersListWithValidData() {
        List<UserDTO> users = getUsers("/users");

        assertThat(users)
                .isNotEmpty()
                .hasSize(6)
                .allSatisfy(user -> {
                    assertThat(user.getId()).isNotNull();
                    assertThat(user.getName()).isNotEmpty();
                    assertThat(user.getSurname()).isNotEmpty();
                    assertThat(user.getUsername()).isNotEmpty();
                });
    }

    @Test
    void shouldReturnUsersListForValidParams() {
        List<UserDTO> users = getUsers("/users?name=john&surname=do");

        assertThat(users).isNotEmpty().hasSize(2);
    }

    @Test
    void shouldReturnEmptyListIfNotFound() {
        List<UserDTO> users = getUsers("/users?username=test_not_found");

        assertThat(users).isEmpty();
    }

    @Test
    void shouldReturnValidationErrorForInvalidParams() {
        String errorMessage = restClient.get()
                .uri("/users?username=u<s>er&name=test_1&surname=Bla'bla")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(errorMessage).isNotEmpty().contains("username", "name");
    }

    @Test
    void shouldReturnSuccessfulHealthCheck() {
        restClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    private List<UserDTO> getUsers(String uri) {
        return restClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(USERS_LIST_TYPE)
                .returnResult()
                .getResponseBody();
    }
}
