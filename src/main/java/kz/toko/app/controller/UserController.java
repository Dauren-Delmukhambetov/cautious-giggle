package kz.toko.app.controller;

import kz.toko.api.UsersApi;
import kz.toko.api.model.CreateUserRequest;
import kz.toko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> createUser(@Valid CreateUserRequest body) {
        userService.createUser(body);
        return ResponseEntity.ok().build();
    }
}
