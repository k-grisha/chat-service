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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class FireBaseService {

    public FireBaseService() {
        try {
            // todo move to environment variable GOOGLE_APPLICATION_CREDENTIALS
            // export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/service-account-file.json"
            URL resource = Thread.currentThread().getContextClassLoader()
                .getResource("georg-94b06-firebase-adminsdk-8glo3-4dc2d8d797.json");
            FileInputStream serviceAccount = new FileInputStream(new File(resource.toURI()));
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Unable to create FireBaseService", e);
        }
    }

    public void sendNotification(String token) {
        var registrationToken = "cm9sgmXsSsulpsEhAcdDb5:APA91bG8lrSpQVtUqEdmk5wnM8woYskMjNklY0gQYYcn1JW5aWUBtY-3CQNfJeHZAgpd954uEAaYqJNHMOypqr4c6e4IPGT8S7f4iV0617an03qMvtUGNO31zN7-XbcuZHDv4EQMjBmL";

        Message message = Message.builder()
            .setNotification(Notification.builder()
                .setBody("Body from Java")
                .setTitle("Title from Java")
                .build())
            .putData("title", "TitleJDK")
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
            .setToken(registrationToken)
            .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Unable to send notification", e);
        }
    }

}
