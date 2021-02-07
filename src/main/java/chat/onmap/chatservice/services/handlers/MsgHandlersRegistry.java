package chat.onmap.chatservice.services.handlers;

import chat.onmap.chatservice.model.MessageType.IncomingDataHandlersRegistry;
import chat.onmap.chatservice.model.MessageType.MsgHandler;
import chat.onmap.chatservice.services.handlers.impl.TextMsgHandler;
import chat.onmap.chatservice.services.handlers.impl.UnknownMsgHandler;
import org.springframework.stereotype.Service;

@Service
public class MsgHandlersRegistry implements IncomingDataHandlersRegistry {

    private final TextMsgHandler textMsgHandler;
    private final UnknownMsgHandler unknownMsgHandler;

    public MsgHandlersRegistry(TextMsgHandler textMsgHandler, UnknownMsgHandler unknownMsgHandler) {
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

}
