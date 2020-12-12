package chat.onmap.chatservice.services.handlers.impl;

import chat.onmap.chatservice.model.Message;
import chat.onmap.chatservice.model.MessageEvent;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.services.handlers.MsgHandlersRegistry.MsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TextMsgHandler implements MsgHandler {

    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TextMsgHandler(MessageRepository messageRepository,
        ApplicationEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Message handleMsg(Message msg) {
        var savedMessage = messageRepository.save(msg);
        eventPublisher.publishEvent(new MessageEvent(savedMessage));
        return savedMessage;
    }

}
