package chat.onmap.chatservice.repository;

import chat.onmap.chatservice.model.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByRecipientAndIdIsAfter(UUID userId, long lastMsgId);

}
