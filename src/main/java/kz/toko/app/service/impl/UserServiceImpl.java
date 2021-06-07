package kz.toko.app.service.impl;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.app.entity.UserEntity;
import kz.toko.app.enumeration.UserStatus;
import kz.toko.app.repository.UserRepository;
import kz.toko.app.service.UserService;
import kz.toko.app.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(CreateUserRequest body) {
        try {
            UserEntity user = new UserEntity();
            user.setUsername(body.getUsername());
            user.setFirstName(body.getFirstName());
            user.setLastName(body.getLastName());
            user.setEmail(body.getEmail());
            user.setPhone(body.getPhone());
            user.setPassword(SecurityUtils.getMD5(body.getPassword()));
            user.setStatus(UserStatus.ACTIVATED);

            userRepository.save(user);
        } catch (Exception e) {
            //todo log
        }
    }
}
