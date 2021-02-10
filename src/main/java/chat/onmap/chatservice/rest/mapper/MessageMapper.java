package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageType;
import chat.onmap.chatservice.rest.dto.MessageDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public abstract class MessageMapper {

    public abstract MessageEntity map(MessageDto dto);

    public abstract MessageDto map(MessageEntity entity);

    public abstract List<MessageDto> map(List<MessageEntity> entitie);

    public MessageType map(int type) {
        return MessageType.valueOf(type);
    }

    public int map(MessageType type) {
        return type == null ? -1 : type.val;
    }

}
