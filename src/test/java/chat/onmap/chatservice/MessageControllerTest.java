package chat.onmap.chatservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.model.MessageType;
import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.repository.UserRepository;
import chat.onmap.chatservice.rest.dto.MessageDto;
import chat.onmap.chatservice.services.FireBaseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private FireBaseService fireBaseService;

    @Test
    void getMessageAsync() throws Exception {
        MessageDto baseMsgDto = MessageDto.builder()
            .senderId(UUID.randomUUID())
            .recipientId(UUID.randomUUID())
            .type(MessageType.TEXT_MSG.val)
            .body("Hi!")
            .build();
        MvcResult mvcResult = mvc.perform(get("/api/v1/message/{uuid}", baseMsgDto.recipientId)
            .param("lastId", "0"))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andReturn();

        // when
        mvc.perform(post("/api/v1/message/{uuid}", baseMsgDto.senderId)
            .content(objectMapper.writeValueAsString(baseMsgDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        // then
        Objects.requireNonNull(mvcResult.getRequest().getAsyncContext()).setTimeout(5000);
        mvcResult.getAsyncResult();

        var json = mvc.perform(asyncDispatch(mvcResult))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        var message = objectMapper.readValue(json, new TypeReference<List<MessageDto>>() {
        });

        assertThat(message).isNotNull();
        assertThat(message.size()).isEqualTo(1);
        assertThat(message.get(0).id).isNotNull();
        assertThat(message.get(0).recipientId).isEqualTo(baseMsgDto.recipientId);
        assertThat(message.get(0).senderId).isEqualTo(baseMsgDto.senderId);
        assertThat(message.get(0).body).isEqualTo(baseMsgDto.body);
        //and
        assertThat(messageRepository.findById(message.get(0).id)).isPresent();
    }


    @Test
    public void sendNotificationTest() throws Exception {
        var offlineRecipient = userRepository.save(ChatUser.builder()
            .name(UUID.randomUUID().toString())
            .fbsMsgToken(UUID.randomUUID().toString())
            .fbsUuid(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .build());
        var sender = userRepository.save(ChatUser.builder()
            .name(UUID.randomUUID().toString())
            .fbsMsgToken(UUID.randomUUID().toString())
            .fbsUuid(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .build());
        var messageDto = MessageDto.builder()
            .senderId(sender.getUuid())
            .recipientId(offlineRecipient.getUuid())
            .type(MessageType.TEXT_MSG.val)
            .body("Hi!")
            .build();
        //when
        var json = mvc.perform(post("/api/v1/message/{uuid}", messageDto.senderId)
            .content(objectMapper.writeValueAsString(messageDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        var message = objectMapper.readValue(json, MessageDto.class);
        assertThat(message).isNotNull();
        assertThat(messageRepository.findById(message.id)).isPresent();
        Mockito.verify(fireBaseService)
            .sendNotification(eq(offlineRecipient.getFbsMsgToken()), anyString(), anyString());
    }


}
