package chat.onmap.chatservice.services;

import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.rest.mapper.MessageMapper;
import org.mapstruct.factory.Mappers;

//@Service
//@Slf4j
public class MessageManager {

    private final MessageRepository messageRepository;
    private final MessageMapper mapper = Mappers.getMapper(MessageMapper.class);

    public MessageManager(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

//    public DeferredResult<List<MessageDto>> waitResult(UUID userId, Long lastMsgId) {
//        DeferredResult<List<MessageDto>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
//        deferredResult.onCompletion(() -> {
//            log.debug("Complete messages request for userId: {}", userId);
//            awaitingRequests.remove(userId);
//        });
//        deferredResult.onError(throwable -> {
//            log.error("Error in messages request for userId: {}", userId);
//            awaitingRequests.remove(userId);
//        });
//        deferredResult.onTimeout(() -> {
//            log.debug("Complete messages request by timeout for userId: {}", userId);
//        });
//        MessagesRequest prevRequest = awaitingRequests.put(userId, new MessagesRequest(lastMsgId, deferredResult));
//        if (prevRequest != null) {
//            log.debug("Request already registered for userId: {}", userId);
//        }
//        return deferredResult;
//    }

//    public DeferredResult<List<MessageDto>> getDeferredResult(UUID userId){
//        DeferredResult<List<MessageDto>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
//        deferredResult.onCompletion(() -> {
//            log.debug("Complete messages request for userId: {}", userId);
//            awaitingRequests.remove(userId);
//        });
//        deferredResult.onError(throwable -> {
//            log.error("Error in messages request for userId: {}", userId);
//            awaitingRequests.remove(userId);
//        });
//        deferredResult.onTimeout(() -> {
//            log.debug("Complete messages request by timeout for userId: {}", userId);
//        });
//        return deferredResult;
//    }

//    @TransactionalEventListener(fallbackExecution = true)
//    public void onNewMessage(MessageEvent event) {
//
//        log.debug("onNewMessage: {}", event);
//        if (event.getSender() != null) {
////            ???
////            requestComplete(event.getSender());
//        }
//        if (event.getRecipient() != null) {
//            requestComplete(event.getRecipient());
//        }
//    }

//    private void requestComplete(UUID userId) {
//        MessagesRequest request = awaitingRequests.get(userId);
//        if (request != null) {
//            List<Message> messages = messageRepository.findAllByRecipientAndIdIsAfter(userId, request.lastMsgId);
////            List<Message> messages = getMessages(userId, request.lastMsgId);
////            log.debug("Complete Comet Request for userId: {}. Msgs Count: {}", userId, messages.size());
//            request.deferredResult.setResult(mapper.map(messages));
//        }
//    }



}
