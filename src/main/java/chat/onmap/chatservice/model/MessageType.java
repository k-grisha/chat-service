package chat.onmap.chatservice.model;


public enum MessageType {
    TEXT_MSG(1) {
        @Override
        public MsgHandlerStrategy getHandler(IncomingDataHandlersRegistry registry) {
            return registry.getTextMessageHandler();
        }
    },

    UNKNOWN_MSG(0) {
        @Override
        public MsgHandlerStrategy getHandler(IncomingDataHandlersRegistry registry) {
            return registry.getUnknownMsgHandler();
        }
    };


    MessageType(int val) {
        this.val = val;
    }

    public final int val;

    public abstract MsgHandlerStrategy getHandler(IncomingDataHandlersRegistry registry);


    public static MessageType valueOf(int longPoolType) {
        for (MessageType val : values()) {
            if (longPoolType == val.val) {
                return val;
            }
        }
        return UNKNOWN_MSG;
    }


    public interface IncomingDataHandlersRegistry {

        MsgHandlerStrategy getTextMessageHandler();

        MsgHandlerStrategy getUnknownMsgHandler();
    }

    public interface MsgHandlerStrategy {

        MessageEntity handleIncomeMsg(MessageEntity msg);

        void handleNotification(MessageEvent msg);
    }
}