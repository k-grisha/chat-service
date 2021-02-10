package chat.onmap.chatservice.services.handlers.impl;


import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.model.MessageType.MsgHandlerStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnknownMsgHandlerStrategy implements MsgHandlerStrategy {

    @Override
    public MessageEntity handleIncomeMsg(MessageEntity msg) {
        log.error("Unknown message received from {} to {}, body:\n{}", msg.getSenderId(), msg.getRecipientId(),
            msg.getBody());
        // todo return error msg
        return msg;
    }

    @Override
    public void handleNotification(MessageEvent msg) {

    }
}
