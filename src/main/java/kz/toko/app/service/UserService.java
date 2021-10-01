package kz.toko.app.service;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(CreateUserRequest body);

    List<User> findAll();

    void delete(Long userId);
}
