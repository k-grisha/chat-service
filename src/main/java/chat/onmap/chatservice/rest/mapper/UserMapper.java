package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.rest.dto.UserDto;
import chat.onmap.chatservice.model.ChatUser;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    ChatUser map(UserDto dto);

    UserDto map(ChatUser model);

}
