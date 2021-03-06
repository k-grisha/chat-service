package chat.onmap.chatservice.rest.controller;

import chat.onmap.chatservice.rest.dto.CreateUserDto;
import chat.onmap.chatservice.rest.dto.UserInfoDto;
import chat.onmap.chatservice.rest.mapper.UserMapper;
import chat.onmap.chatservice.services.UserService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class UserController {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UserInfoDto saveUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return mapper.map(userService.saveUser(createUserDto.name, createUserDto.fbsMsgToken, createUserDto.fbsJwt));
    }

    @GetMapping("/user/{uuid}")
    public UserInfoDto getUser(@PathVariable UUID uuid) {
        return mapper.map(userService.getUser(uuid));
    }

//    @PutMapping("/user/{uuid}")
//    public UserDto updateUser(@PathVariable UUID uuid, @Valid @RequestBody UserDto userDto) {
//        return mapper.map(userService.updateUser(uuid, mapper.map(userDto)));
//    }

}
