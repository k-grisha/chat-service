package chat.onmap.chatservice.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageType;
import chat.onmap.chatservice.rest.dto.MessageDto;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MessageMapperTest {

    private final MessageMapper mapper = Mappers.getMapper(MessageMapper.class);

    @Test
    public void testDtoToEntity() {
        var dto = MessageDto.builder()
            .id(new Random().nextLong())
            .recipientId(UUID.randomUUID())
            .senderId(UUID.randomUUID())
            .type(1)
            .build();
        var entity = mapper.map(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getType()).isEqualTo(MessageType.TEXT_MSG);
        assertThat(entity.getId()).isEqualTo(dto.id);
        assertThat(entity.getRecipientId()).isEqualTo(dto.recipientId);
        assertThat(entity.getSenderId()).isEqualTo(dto.senderId);
    }

    @Test
    public void testEntitiesToDtos() {
        var msg = MessageEntity.builder()
            .id(new Random().nextLong())
            .recipientId(UUID.randomUUID())
            .senderId(UUID.randomUUID())
            .build();
        var dtos = mapper.map(Collections.singletonList(msg));
        assertThat(dtos).isNotNull();
        assertThat(dtos.size()).isEqualTo(1);
        assertThat(dtos.get(0).id).isEqualTo(msg.getId());
    }
}