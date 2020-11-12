package chat.onmap.chatservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chat.onmap.chatservice.repository.MessageRepository;
import chat.onmap.chatservice.rest.BaseResponse;
import chat.onmap.chatservice.rest.dto.MessageDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void getMessageAsync() throws Exception {
        MessageDto messageDto = MessageDto.builder()
            .sender(UUID.randomUUID())
            .recipient(UUID.randomUUID())
            .message("Hi!")
            .build();
        MvcResult mvcResult = mvc.perform(get("/api/v1/message/{uuid}", messageDto.recipient)
            .param("lastId", "0"))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andReturn();

        mvc.perform(post("/api/v1/message/{uuid}", messageDto.sender)
            .content(objectMapper.writeValueAsString(messageDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        Objects.requireNonNull(mvcResult.getRequest().getAsyncContext()).setTimeout(5000);
        mvcResult.getAsyncResult();

        var json = mvc.perform(asyncDispatch(mvcResult))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        var message = objectMapper.readValue(json, new TypeReference<BaseResponse<List<MessageDto>>>() {
        });

        assertThat(message).isNotNull();
        assertThat(message.isSuccess()).isTrue();
        assertThat(message.getBody()).isNotNull();
        assertThat(message.getBody().size()).isEqualTo(1);
        assertThat(message.getBody().get(0).id).isNotNull();
        assertThat(message.getBody().get(0).recipient).isEqualTo(messageDto.recipient);
        assertThat(message.getBody().get(0).sender).isEqualTo(messageDto.sender);
        assertThat(message.getBody().get(0).message).isEqualTo(messageDto.message);

    }

}
