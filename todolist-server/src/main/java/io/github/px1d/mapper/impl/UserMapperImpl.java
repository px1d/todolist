package io.github.px1d.mapper.impl;

import io.github.px1d.data.req.UserCreateRequest;
import io.github.px1d.data.resp.LoginResponse;
import io.github.px1d.data.vo.UserVO;
import io.github.px1d.mapper.UserMapper;
import io.github.px1d.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toModel(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    @Override
    public UserVO toVO(User user) {
        if (user == null) {
            return null;
        }

        return UserVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Override
    public LoginResponse toLoginResponse(User user) {
        if (user == null) {
            return null;
        }

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
