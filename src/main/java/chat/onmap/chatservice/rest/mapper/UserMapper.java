package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.rest.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface UserMapper {

    @Mappings({
        @Mapping(target = "fireBaseToken", source = "fbsToken")
    })
    ChatUser map(UserDto dto);

    @Mappings({
        @Mapping(target = "fbsToken", source = "fireBaseToken")
    })
    UserDto map(ChatUser model);

}
