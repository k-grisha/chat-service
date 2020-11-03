package chat.onmap.chatservice.repository;

import chat.onmap.chatservice.model.ChatUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ChatUser, UUID> {

    Optional<ChatUser> findAllByName(String name);
}
