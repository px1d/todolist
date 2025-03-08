package io.github.px1d.controller;

import io.github.px1d.auth.annotation.RequiresAuth;
import io.github.px1d.data.req.LoginRequest;
import io.github.px1d.data.req.UserCreateRequest;
import io.github.px1d.data.resp.LoginResponse;
import io.github.px1d.data.vo.UserVO;
import io.github.px1d.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API for managing users")
public class UserController {

    private final UserService userService;

    @RequiresAuth
    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserVO> createUser(
            @Parameter(description = "User information", required = true) @Valid @RequestBody UserCreateRequest UserVO) {
        return new ResponseEntity<>(userService.createUser(UserVO), HttpStatus.CREATED);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Login credentials", required = true) @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
