package io.github.px1d.service;

import io.github.px1d.model.User;
import io.github.px1d.data.req.LoginRequest;
import io.github.px1d.data.req.UserCreateRequest;
import io.github.px1d.data.resp.LoginResponse;
import io.github.px1d.data.vo.UserVO;
import io.github.px1d.exception.AuthenticationException;
import io.github.px1d.mapper.UserMapper;
import io.github.px1d.repository.UserRepository;
import io.github.px1d.auth.util.JwtUtil;
import io.github.px1d.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserVO createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }
        User user = userMapper.toModel(request);
        user.setSalt(PasswordUtil.generateSalt());
        user.setHashedPassword(PasswordUtil.hashPassword(request.getPassword(), user.getSalt()));
        User savedUser = userRepository.create(user);
        return userMapper.toVO(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        boolean isAuthenticated = PasswordUtil.verifyPassword(
                request.getPassword(),
                user.getSalt(),
                user.getHashedPassword()
        );

        if (!isAuthenticated) {
            throw new AuthenticationException("Invalid email or password");
        }

        LoginResponse loginResponse = userMapper.toLoginResponse(user);
        String token = jwtUtil.generateToken(user);
        loginResponse.setToken(token);

        return loginResponse;
    }
}
