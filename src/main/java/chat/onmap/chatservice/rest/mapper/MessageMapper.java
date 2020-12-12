package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.model.MessageType;
import chat.onmap.chatservice.rest.dto.MessageDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public abstract class MessageMapper {

    //    @Mappings({
//        @Mapping(target="type", expression = "java( MessageType. )")
//    })
    public abstract Message map(MessageDto dto);

    public abstract MessageDto map(Message entity);

    public abstract List<MessageDto> map(List<Message> entitie);

    public MessageType map(int type) {
        return MessageType.valueOf(type);
    }

    public int map(MessageType type) {
        return type == null ? -1 : type.val;
    }

}
