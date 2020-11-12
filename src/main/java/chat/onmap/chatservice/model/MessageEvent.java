package chat.onmap.chatservice.model;

import java.util.UUID;
import lombok.Data;

@Data
public class MessageEvent {

    private final long msgId;
    private final UUID sender;
    private final UUID recipient;

    public MessageEvent(Message message) {
        this.msgId = message.getId();
        this.sender = message.getSender();
        this.recipient = message.getRecipient();
    }
}
