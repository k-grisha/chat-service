package chat.onmap.chatservice.rest.dto;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class MessageDto {

    public final long id;
    @NotNull
    public final UUID sender;
    @NotNull
    public final UUID recipient;
    @NotBlank
    public final String message;

}
