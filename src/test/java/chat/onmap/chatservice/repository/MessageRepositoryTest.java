package chat.onmap.chatservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chat.onmap.chatservice.model.Message;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void test() {
        var recipient = UUID.randomUUID();
        var msg1 = generateDetachedMessageForRecipient(recipient);
        var msg2 = generateDetachedMessageForRecipient(recipient);
        var msg3 = generateDetachedMessageForRecipient(recipient);
        var savedMessage1 = messageRepository.save(msg1);
        messageRepository.save(msg2);
        messageRepository.save(msg3);

        List<Message> messages = messageRepository
            .findAllByRecipientAndIdIsAfter(recipient, savedMessage1.getId());
        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getMessage()).isEqualTo(msg2.getMessage());
        assertThat(messages.get(1).getMessage()).isEqualTo(msg3.getMessage());
    }

    private Message generateDetachedMessageForRecipient(UUID recipient) {
        var rnd = new Random();
        return Message.builder()
            .recipient(recipient)
            .sender(UUID.randomUUID())
            .message(UUID.randomUUID().toString())
            .build();
    }
}