package chat.onmap.chatservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chat.onmap.chatservice.model.MessageEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void messageRepositoryTest() {
        var recipient = UUID.randomUUID();
        var msg1 = generateDetachedMessageForRecipient(recipient);
        var msg2 = generateDetachedMessageForRecipient(recipient);
        var msg3 = generateDetachedMessageForRecipient(recipient);
        var savedMessage1 = messageRepository.save(msg1);
        messageRepository.save(msg2);
        messageRepository.save(msg3);

        List<MessageEntity> messageEntities = messageRepository
            .findAllByRecipientIdAndIdIsAfter(recipient, savedMessage1.getId());
        assertThat(messageEntities).hasSize(2);
        assertThat(messageEntities.get(0).getBody()).isEqualTo(msg2.getBody());
        assertThat(messageEntities.get(1).getBody()).isEqualTo(msg3.getBody());
    }

    private MessageEntity generateDetachedMessageForRecipient(UUID recipientId) {
        return MessageEntity.builder()
            .recipientId(recipientId)
            .senderId(UUID.randomUUID())
            .body(UUID.randomUUID().toString())
            .build();
    }
}