package chat.onmap.chatservice.services.handlers.impl;

import chat.onmap.chatservice.model.MessageEntity;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.model.MessageType.MsgHandlerStrategy;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.repository.UserRepository;
import chat.onmap.chatservice.services.FireBaseService;
import java.util.Map;
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
        userRepository.findById(msg.getSenderId()).ifPresentOrElse(
            sender -> userRepository.findById(msg.getRecipientId()).ifPresentOrElse(
                recipient -> fireBaseService.sendNotification(
                    recipient.getFbsMsgToken(),
                    "New message from " + sender.getName(),
                    msg.getBody().substring(0, Math.min(msg.getBody().length(), 50))),
                () -> log.warn("Recipient for notification not found. {}", msg.getRecipientId())
            ),
            () -> log.warn("Sender for notification not found {}", msg.getSenderId())
        );
    }

}
