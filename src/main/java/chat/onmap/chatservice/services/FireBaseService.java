package chat.onmap.chatservice.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FireBaseService {

    public FireBaseService() {
        try {
            // todo move to environment variable GOOGLE_APPLICATION_CREDENTIALS
            // export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/service-account-file.json"
            var is = FireBaseService.class.getClassLoader()
                .getResourceAsStream("georg-94b06-firebase-adminsdk-8glo3-da70cef433.json");
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(is))
                .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create FireBaseService", e);
        }
    }

    public void sendNotification(String token, String title, String body) {
        sendNotification(token, title, body, Map.of());
    }

    public void sendNotification(String token, String title, String body, Map<String, String> data) {
        final FirebaseMessaging firebaseMessaging;
        try {
            firebaseMessaging = FirebaseMessaging.getInstance();
        } catch (Exception e) {
            log.error("FireBaseService is not initialised", e);
            return;
        }
//        var registrationToken = "cm9sgmXsSsulpsEhAcdDb5:APA91bG8lrSpQVtUqEdmk5wnM8woYskMjNklY0gQYYcn1JW5aWUBtY-3CQNfJeHZAgpd954uEAaYqJNHMOypqr4c6e4IPGT8S7f4iV0617an03qMvtUGNO31zN7-XbcuZHDv4EQMjBmL";
        var messageBuilder = Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
            .setToken(token);
        data.forEach(messageBuilder::putData);

        try {
            String response = firebaseMessaging.send(messageBuilder.build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Unable to send notification", e);
        }
    }

}
