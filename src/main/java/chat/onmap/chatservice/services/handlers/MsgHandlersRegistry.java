package chat.onmap.chatservice.services.handlers;

import chat.onmap.chatservice.model.MessageType.IncomingDataHandlersRegistry;
import chat.onmap.chatservice.model.MessageType.MsgHandlerStrategy;
import chat.onmap.chatservice.services.handlers.impl.TextMsgHandlerStrategy;
import chat.onmap.chatservice.services.handlers.impl.UnknownMsgHandlerStrategy;
import org.springframework.stereotype.Service;

@Service
public class MsgHandlersRegistry implements IncomingDataHandlersRegistry {

    private final TextMsgHandlerStrategy textMsgHandler;
    private final UnknownMsgHandlerStrategy unknownMsgHandler;

    public MsgHandlersRegistry(TextMsgHandlerStrategy textMsgHandler, UnknownMsgHandlerStrategy unknownMsgHandler) {
        this.textMsgHandler = textMsgHandler;
        this.unknownMsgHandler = unknownMsgHandler;
    }

    @Override
    public MsgHandlerStrategy getTextMessageHandler() {
        return textMsgHandler;
    }

    @Override
    public MsgHandlerStrategy getUnknownMsgHandler() {
        return unknownMsgHandler;
    }

}
