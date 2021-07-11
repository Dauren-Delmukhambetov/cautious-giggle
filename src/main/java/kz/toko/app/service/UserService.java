package kz.toko.app.service;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.User;

import java.util.List;

public interface UserService {

    User save(CreateUserRequest body);

    List<User> findAll();

    void delete(Long userId);
}
