package chat.onmap.chatservice.services.handlers.impl;


import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.services.handlers.MsgHandlersRegistry.MsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnknownMsgHandler implements MsgHandler {

    @Override
    public Message handleMsg(Message msg) {
        log.error("Unknown message received from {} to {}, body:\n{}", msg.getSender(), msg.getRecipient(),
            msg.getBody());
        // todo return error msg
        return msg;
    }
}
