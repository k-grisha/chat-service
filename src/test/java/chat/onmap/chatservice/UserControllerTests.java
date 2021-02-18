package chat.onmap.chatservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.repository.UserRepository;
import chat.onmap.chatservice.services.FireBaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FireBaseService fireBaseService;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @Test
    void getUserTest() throws Exception {
        var ivan = userRepository
            .save(ChatUser.builder().name("Ivan").fireBaseToken(UUID.randomUUID().toString()).build());
        var json = mvc.perform(get("/api/v1/user/{uuid}", ivan.getUuid()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(json, ChatUser.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(ivan.getName());
        assertThat(result.getUuid()).isEqualTo(ivan.getUuid());
    }

    @Test
    void saveUserTest() throws Exception {
        var json = mvc.perform(
            post("/api/v1/user")
                .content("{\"name\": \"qweasd\", \"fbsToken\": \"ae446a34-4975-4de1-9c2f-9995859f5d9f\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(json, ChatUser.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("qweasd");
    }

    @Test
    void updateUserTest() throws Exception {
        var ivan = userRepository
            .save(ChatUser.builder().name("Ivan").fireBaseToken(UUID.randomUUID().toString()).build());
        var json = mvc.perform(
            put("/api/v1/user/{uuid}", ivan.getUuid())
                .content("{\"name\": \"Stepan\", \"fbsToken\": \"ae446a34-4975-4de1-9c2f-9995859f5d8f\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(json, ChatUser.class);
        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isEqualTo(ivan.getUuid());
        assertThat(result.getName()).isEqualTo("Stepan");
    }


}
