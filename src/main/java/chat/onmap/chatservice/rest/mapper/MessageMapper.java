package chat.onmap.chatservice.rest.mapper;

import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.rest.dto.MessageDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface MessageMapper {

    Message map(MessageDto dto);

    MessageDto map(Message entity);

    List<MessageDto> map (List<Message> entities);

}
