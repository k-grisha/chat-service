package chat.onmap.chatservice.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import chat.onmap.chatservice.model.Message;
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
            .recipient(UUID.randomUUID())
            .sender(UUID.randomUUID())
            .build();
        var entity = mapper.map(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.id);
        assertThat(entity.getRecipient()).isEqualTo(dto.recipient);
        assertThat(entity.getSender()).isEqualTo(dto.sender);
    }

    @Test
    public void testEntitiesToDtos() {
        var msg = Message.builder()
            .id(new Random().nextLong())
            .recipient(UUID.randomUUID())
            .sender(UUID.randomUUID())
            .build();
        var dtos = mapper.map(Collections.singletonList(msg));
        assertThat(dtos).isNotNull();
        assertThat(dtos.size()).isEqualTo(1);
        assertThat(dtos.get(0).id).isEqualTo(msg.getId());
    }
}