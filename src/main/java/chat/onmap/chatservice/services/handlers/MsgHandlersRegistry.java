package chat.onmap.chatservice.services.handlers;

import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.model.MessageType.IncomingDataHandlersRegistry;
import chat.onmap.chatservice.services.handlers.impl.UnknownMsgHandler;
import chat.onmap.chatservice.services.handlers.impl.TextMsgHandler;
import org.springframework.stereotype.Service;

@Service
public class MsgHandlersRegistry implements IncomingDataHandlersRegistry {

    private final TextMsgHandler textMsgHandler;
    private final UnknownMsgHandler unknownMsgHandler;

    public MsgHandlersRegistry(TextMsgHandler textMsgHandler,
        UnknownMsgHandler unknownMsgHandler) {
        this.textMsgHandler = textMsgHandler;
        this.unknownMsgHandler = unknownMsgHandler;
    }

    @Override
    public MsgHandler getTextMessageHandler() {
        return textMsgHandler;
    }

    @Override
    public MsgHandler getUnknownMsgHandler() {
        return unknownMsgHandler;
    }

    public interface MsgHandler {

        Message handleMsg(Message msg);
    }
}
