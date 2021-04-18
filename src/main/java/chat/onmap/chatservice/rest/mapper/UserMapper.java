package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.rest.dto.UserInfoDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    //    @Mappings({
//        @Mapping(target = "fbsToken", source = "fireBaseToken")
//    })
    UserInfoDto map(ChatUser model);

}
