package kz.toko.app.service;

import kz.toko.api.model.CreateUserRequest;

public interface UserService {

    void createUser(CreateUserRequest body);
}
