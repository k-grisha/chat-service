package chat.onmap.chatservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.repository.UserRepository;
import chat.onmap.chatservice.services.FireBaseService;
import chat.onmap.chatservice.services.FireBaseService.FbsDecodedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
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

    @Autowired
    ApplicationContext context;

    @Test
    void asdfg() {
        when(fireBaseService.verifyDecodeJwt("ae446a34-4975-4de1-9c2f-9995859f5d9f"))
            .thenReturn(new FbsDecodedUser(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString()));
        var service = context.getBean(FireBaseService.class);
        var user = service.verifyDecodeJwt("ae446a34-4975-4de1-9c2f-9995859f5d9f");

    }

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @Test
    void getUserTest() throws Exception {
        var ivan = userRepository
            .save(ChatUser.builder()
                .name("Ivan")
                .email("email@mail.com")
                .fbsUuid(UUID.randomUUID().toString())
                .fbsMsgToken(UUID.randomUUID().toString()).build());
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
    void registerNewUserTest() throws Exception {
        var name = "Ivan";
        when(fireBaseService.verifyDecodeJwt("ae446a34-4975-4de1-9c2f-9995859f5d9f"))
            .thenReturn(new FbsDecodedUser(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString()));
        var json = mvc.perform(
            post("/api/v1/user")
                .content("{\"name\": \"" + name
                    + "\", \"fbsMsgToken\": \"ae446a34-4975-4de1-9c2f-9995859f5d9f\", \"fbsJwt\": \"ae446a34-4975-4de1-9c2f-9995859f5d9f\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        var result = objectMapper.readValue(json, ChatUser.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    void reRegisterExistingUserTest() throws Exception {

        var user = userRepository.save(ChatUser.builder()
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .fbsMsgToken(UUID.randomUUID().toString())
            .fbsUuid(UUID.randomUUID().toString())
            .build());
        when(fireBaseService.verifyDecodeJwt("ae446a34-4975-4de1-9c2f-9995859f5d9f"))
            .thenReturn(new FbsDecodedUser(user.getFbsUuid(), user.getEmail(), user.getPicture()));
        var json = mvc.perform(
            post("/api/v1/user")
                .content("{\"name\": \"" + UUID.randomUUID()
                    + "\", \"fbsMsgToken\": \"" + user.getFbsMsgToken()
                    + "\", \"fbsJwt\": \"ae446a34-4975-4de1-9c2f-9995859f5d9f\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(json, ChatUser.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(user.getName());
    }

//    @Test
//    void updateUserTest() throws Exception {
//        var ivan = userRepository
//            .save(ChatUser.builder().name("Ivan").fbsMsgToken(UUID.randomUUID().toString()).build());
//        var json = mvc.perform(
//            put("/api/v1/user/{uuid}", ivan.getUuid())
//                .content("{\"name\": \"Stepan\", \"fbsToken\": \"ae446a34-4975-4de1-9c2f-9995859f5d8f\"}")
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andDo(MockMvcResultHandlers.print())
//            .andExpect(status().isOk())
//            .andReturn().getResponse().getContentAsString();
//        var result = objectMapper.readValue(json, ChatUser.class);
//        assertThat(result).isNotNull();
//        assertThat(result.getUuid()).isEqualTo(ivan.getUuid());
//        assertThat(result.getName()).isEqualTo("Stepan");
//    }


}
