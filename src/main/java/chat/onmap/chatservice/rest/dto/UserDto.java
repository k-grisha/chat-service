package chat.onmap.chatservice.rest.dto;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserDto {

    public final UUID uuid;

    @NotNull(message = "Name is mandatory")
    @Size(min = 3, max = 100, message = "Name length should be from 3 up to 100 symbols")
    public final String name;
}
