package chat.onmap.chatservice.services;


import static chat.onmap.chatservice.exception.ChatOnMapException.ErrorCodes.NOT_FOUND;
import static chat.onmap.chatservice.exception.ChatOnMapException.ErrorCodes.USER_ALREADY_EXIST;

import chat.onmap.chatservice.exception.ChatOnMapException;
import chat.onmap.chatservice.model.ChatUser;
import chat.onmap.chatservice.repository.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatUser saveUser(ChatUser chatUser) {
        userRepository.findAllByName(chatUser.getName()).ifPresent(user -> {
            throw new ChatOnMapException("User with name '" + user.getName() + "' already exist", USER_ALREADY_EXIST);
        });

        return userRepository.save(chatUser);
    }

    public ChatUser getUser(UUID uuid) {
        return userRepository.findById(uuid)
            .orElseThrow(() -> new ChatOnMapException("User with ID " + uuid + " not found", NOT_FOUND));
    }

    @Transactional
    public ChatUser updateUser(UUID uuid, ChatUser newChatUser) {
        userRepository.findAllByName(newChatUser.getName()).ifPresent(user -> {
            throw new ChatOnMapException("User with name '" + user.getName() + "' already exist", USER_ALREADY_EXIST);
        });
        ChatUser chatUser = userRepository.findById(uuid).orElseThrow(
            () -> new ChatOnMapException("User with ID " + newChatUser.getUuid() + " not found", NOT_FOUND));
        chatUser.setName(newChatUser.getName());
        return chatUser;
    }
}
