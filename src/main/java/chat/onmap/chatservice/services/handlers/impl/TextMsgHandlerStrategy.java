package chat.onmap.chatservice.services.handlers.impl;

import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.model.MessageType.MsgHandlerStrategy;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.repository.UserRepository;
import chat.onmap.chatservice.services.FireBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TextMsgHandlerStrategy implements MsgHandlerStrategy {

    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FireBaseService fireBaseService;
    private final UserRepository userRepository;

    public TextMsgHandlerStrategy(MessageRepository messageRepository, ApplicationEventPublisher eventPublisher,
        FireBaseService fireBaseService, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.eventPublisher = eventPublisher;
        this.fireBaseService = fireBaseService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public MessageEntity handleIncomeMsg(MessageEntity msg) {
        if (msg.getBody().trim().isBlank()) {
            log.warn("empty message declined");
            return msg;
        }
        var savedMessage = messageRepository.save(msg);
        eventPublisher.publishEvent(new MessageEvent(savedMessage));
        return savedMessage;
    }

    @Override
    public void handleNotification(MessageEvent msg) {
        userRepository.findById(msg.getRecipientId()).ifPresentOrElse(
            chatUser -> fireBaseService.sendNotification(chatUser.getFireBaseToken()),
            () -> log.warn("User {} for notification not found", msg.getRecipientId())
        );

    }

}
