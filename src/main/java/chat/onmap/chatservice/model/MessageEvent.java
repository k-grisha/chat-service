package chat.onmap.chatservice.model;

import java.util.UUID;
import lombok.Data;

@Data
public class MessageEvent {

    private final long msgId;
    private final UUID senderId;
    private final UUID recipientId;
    private final MessageType type;
    private final String body;

    public MessageEvent(MessageEntity messageEntity) {
        this.msgId = messageEntity.getId();
        this.senderId = messageEntity.getSenderId();
        this.recipientId = messageEntity.getRecipientId();
        this.type = messageEntity.getType();
        this.body = messageEntity.getBody();
    }
}
