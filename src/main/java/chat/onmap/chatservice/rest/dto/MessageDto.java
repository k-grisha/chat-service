package chat.onmap.chatservice.rest.dto;

import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class MessageDto {

    public final long id;
    @NotNull
    public final UUID senderId;
    @NotNull
    public final UUID recipientId;
    @Min(1)
    public final int type;
    @NotBlank
    public final String body;
}
