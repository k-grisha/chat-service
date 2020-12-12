package chat.onmap.chatservice.model;

import chat.onmap.chatservice.services.handlers.MsgHandlersRegistry.MsgHandler;

public enum MessageType {
    TEXT_MSG(1) {
        @Override
        public MsgHandler getHandler(IncomingDataHandlersRegistry registry) {
            return registry.getTextMessageHandler();
        }
    },

    UNKNOWN_MSG(0) {
        @Override
        public MsgHandler getHandler(IncomingDataHandlersRegistry registry) {
            return registry.getUnknownMsgHandler();
        }
    };
//        PHOTO_MSG(2),
//        MSG_STATUS(3);

    MessageType(int val) {
        this.val = val;
    }

    public final int val;


    public abstract MsgHandler getHandler(IncomingDataHandlersRegistry registry);


    public static MessageType valueOf(int longPoolType) {
        for (MessageType val : values()) {
            if (longPoolType == val.val) {
                return val;
            }
        }
        return UNKNOWN_MSG;
//        throw new IllegalArgumentException("No matching constant for [" + longPoolType + "]");
    }


    public interface IncomingDataHandlersRegistry {

        MsgHandler getTextMessageHandler();

        MsgHandler getUnknownMsgHandler();
    }

}