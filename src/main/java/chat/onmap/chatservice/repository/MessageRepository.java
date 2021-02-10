package chat.onmap.chatservice.repository;

import chat.onmap.chatservice.model.MessageEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByRecipientIdAndIdIsAfter(UUID recipientId, long lastMsgId);

}
