package chat.onmap.chatservice.services;

import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.rest.dto.MessageDto;
import chat.onmap.chatservice.rest.mapper.MessageMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@Slf4j
public class MessageService {

    private final Map<UUID, MessagesRequest> waitingRequestsMap = new ConcurrentHashMap<>();
    private final MessageMapper mapper = Mappers.getMapper(MessageMapper.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    // todo get rid of DTO
    public DeferredResult<List<MessageDto>> getMessages(final UUID userId, final Long startId) {
        DeferredResult<List<MessageDto>> deferredResult = getDeferredResult(userId);
        List<Message> messages = messageRepository.findAllByRecipientAndIdIsAfter(userId, startId);
        if (!messages.isEmpty()) {
            deferredResult.setResult(mapper.map(messages));
        } else {
            waitingRequestsMap.put(userId, new MessagesRequest(startId, deferredResult));
        }
        return deferredResult;
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void onNewMessage(MessageEvent event) {
        log.debug("onNewMessage: {}", event);
        if (event.getRecipient() != null) {
            Optional.ofNullable(waitingRequestsMap.get(event.getRecipient()))
                .ifPresentOrElse(waitingRequest -> {
                    List<Message> messages = messageRepository
                        .findAllByRecipientAndIdIsAfter(event.getRecipient(), waitingRequest.lastMsgId);
                    waitingRequest.deferredResult.setResult(mapper.map(messages));
                }, () -> {
                    System.out.println(event);
                });
        }
    }

    private DeferredResult<List<MessageDto>> getDeferredResult(UUID userId) {
        DeferredResult<List<MessageDto>> deferredResult = new DeferredResult<>(null, Collections.emptyList());
        deferredResult.onCompletion(() -> {
            log.debug("Complete messages request for userId: {}", userId);
            waitingRequestsMap.remove(userId);
        });
        deferredResult.onError(throwable -> {
            log.error("Error in messages request for userId: {}", userId);
            waitingRequestsMap.remove(userId);
        });
        deferredResult.onTimeout(() -> {
            log.debug("Complete messages request by timeout for userId: {}", userId);
        });
        return deferredResult;
    }

    @AllArgsConstructor
    private static final class MessagesRequest {

        public final Long lastMsgId;
        public final DeferredResult<List<MessageDto>> deferredResult;
    }
}
