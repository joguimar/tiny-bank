package es.jguimar.tinybankAPI.adapter.rest;

import es.jguimar.tinybankAPI.adapter.rest.dto.UserRequestDto;
import es.jguimar.tinybankAPI.adapter.rest.dto.UserResponseDto;
import es.jguimar.tinybankAPI.adapter.rest.tranform.UserMapper;
import es.jguimar.tinybankAPI.application.port.inbound.UserWeb;
import es.jguimar.tinybankAPI.application.service.CreateUserService;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceExistsException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Api(value = "API tiny bank poc", tags = { "User" })
@AllArgsConstructor //Constructor-Based Dependency Injection
public class UserController implements UserWeb {

    private final CreateUserService createUserService;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto userPost(@Valid UserRequestDto body) throws ResourceExistsException {
        return userMapper.toUserResponseDto(
                createUserService.create(userMapper.toUser(body)));
    }
}
