package chat.onmap.chatservice.rest.controller;

import chat.onmap.chatservice.model.MessageType;
import chat.onmap.chatservice.rest.dto.MessageDto;
import chat.onmap.chatservice.rest.mapper.MessageMapper;
import chat.onmap.chatservice.services.MessageService;
import chat.onmap.chatservice.services.handlers.MsgHandlersRegistry;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class MessageController {

    private final MessageMapper mapper = Mappers.getMapper(MessageMapper.class);
    private final MessageService messageService;
    private final MsgHandlersRegistry msgHandlersRegistry;

    public MessageController(MessageService messageService,
        MsgHandlersRegistry msgHandlersRegistry) {
        this.messageService = messageService;
        this.msgHandlersRegistry = msgHandlersRegistry;
    }

    @GetMapping("message/{userId}")
    public DeferredResult<List<MessageDto>> getMessages(@PathVariable(name = "userId") UUID userId,
        @RequestParam(name = "lastId") Long lastId) {
        return messageService.getMessages(userId, lastId);
    }

    @PostMapping("message/{userId}")
    public MessageDto saveMessage(@PathVariable(name = "userId") UUID userId, @RequestBody MessageDto messageDto) {
        // todo add security verification of userId

        return mapper.map(
            MessageType.valueOf(messageDto.type).getHandler(msgHandlersRegistry).handleIncomeMsg(mapper.map(messageDto)));
    }

}
