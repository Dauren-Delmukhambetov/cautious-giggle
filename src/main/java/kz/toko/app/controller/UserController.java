package kz.toko.app.controller;

import kz.toko.api.UsersApi;
import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.UpdateUserRequest;
import kz.toko.api.model.User;
import kz.toko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(@Valid CreateUserRequest body) {
        return ok(userService.save(body));
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        return ok(userService.findAll());
    }

    @Override
    public ResponseEntity<Void> updateUser(Long id, UpdateUserRequest body) {
        userService.update(id, body);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.delete(userId);
        return noContent().build();
    }

    @Override
    public ResponseEntity<User> getUser(Long id) {
        return ok(userService.findById(id));
    }
}
