package chat.onmap.chatservice.services;

import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.rest.dto.MessageDto;
import chat.onmap.chatservice.rest.mapper.MessageMapper;
import chat.onmap.chatservice.services.handlers.MsgHandlersRegistry;
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

    private final Map<UUID, WaitingRequest> waitingRequestsMap = new ConcurrentHashMap<>();
    private final MessageMapper mapper = Mappers.getMapper(MessageMapper.class);
    private final MessageRepository messageRepository;
    private final FireBaseService fireBaseService;
    private final MsgHandlersRegistry msgHandlersRegistry;

    public MessageService(MessageRepository messageRepository, FireBaseService fireBaseService,
        MsgHandlersRegistry msgHandlersRegistry) {
        this.messageRepository = messageRepository;
        this.fireBaseService = fireBaseService;
        this.msgHandlersRegistry = msgHandlersRegistry;
    }

    @Transactional(readOnly = true)
    // todo get rid of DTO
    public DeferredResult<List<MessageDto>> getMessages(final UUID userId, final Long startId) {
        DeferredResult<List<MessageDto>> deferredResult = buildDeferredResult(userId);
        List<MessageEntity> messageEntities = messageRepository.findAllByRecipientIdAndIdIsAfter(userId, startId);
        if (!messageEntities.isEmpty()) {
            deferredResult.setResult(mapper.map(messageEntities));
        } else {
            waitingRequestsMap.put(userId, new WaitingRequest(userId, startId, deferredResult));
        }
        return deferredResult;
    }

    public MessageEntity handleIncomeMsg(MessageEntity messageEntity) {
        return messageEntity.getType().getHandler(msgHandlersRegistry).handleIncomeMsg(messageEntity);
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void onNewMessage(MessageEvent event) {
        log.debug("onNewMessage: {}", event);
        if (event.getRecipientId() != null) {
            Optional.ofNullable(waitingRequestsMap.get(event.getRecipientId()))
                .ifPresentOrElse(this::sendMessage, () -> sendNotification(event));
        }
    }

    private void sendMessage(WaitingRequest waitingRequest) {
        List<MessageEntity> messageEntities = messageRepository
            .findAllByRecipientIdAndIdIsAfter(waitingRequest.userId, waitingRequest.lastMsgId);
        waitingRequest.deferredResult.setResult(mapper.map(messageEntities));
    }

    private void sendNotification(MessageEvent messageEvent) {
        messageEvent.getType().getHandler(msgHandlersRegistry).handleNotification(messageEvent);
    }

    private DeferredResult<List<MessageDto>> buildDeferredResult(UUID userId) {
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
    private static final class WaitingRequest {

        public UUID userId;
        public final Long lastMsgId;
        public final DeferredResult<List<MessageDto>> deferredResult;
    }
}
