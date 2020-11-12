package chat.onmap.chatservice.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import chat.onmap.chatservice.rest.dto.UserDto;
import chat.onmap.chatservice.rest.mapper.UserMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void test() {
        var dto = UserDto.builder().name(UUID.randomUUID().toString()).build();
        var user = mapper.map(dto);
        assertThat(user.getName()).isEqualTo(dto.name);
    }
}