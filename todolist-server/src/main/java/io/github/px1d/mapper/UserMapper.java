package io.github.px1d.mapper;

import io.github.px1d.data.req.UserCreateRequest;
import io.github.px1d.data.resp.LoginResponse;
import io.github.px1d.data.vo.UserVO;
import io.github.px1d.model.User;

public interface UserMapper {

    User toModel(UserCreateRequest request);

    UserVO toVO(User user);

    LoginResponse toLoginResponse(User user);
}
