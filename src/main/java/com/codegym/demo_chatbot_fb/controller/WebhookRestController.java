package com.codegym.demo_chatbot_fb.controller;

import com.codegym.demo_chatbot_fb.model.User;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.webhook.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.ArrayList;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RestController
public class WebhookRestController {
    private ArrayList<User> users = new ArrayList<>();
    private static final String RESOURCE_URL = "https://raw.githubusercontent.com/fbsamples/messenger-platform-samples/master/node/public";

    private static final Logger logger = LoggerFactory.getLogger(WebhookRestController.class);

    private final Messenger messenger;

    @Autowired
    public WebhookRestController(final Messenger messenger) {
        this.messenger = messenger;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(/*@RequestParam("hub.mode") final String mode,
                                                @RequestParam("hub.verify_token") final String verifyToken, */
                                                @RequestParam("hub.challenge") final String challenge) {
        return ResponseEntity.ok(challenge);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerVerificationException {
        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        this.messenger.onReceiveEvents(payload, of(signature), event -> {
            try {
                handleTextMessageEvent(event.asTextMessageEvent());
                logger.info("1");
            } catch (MessengerApiException e) {
                logger.info("2");
                e.printStackTrace();
            } catch (MessengerIOException e) {
                logger.info("3");
                e.printStackTrace();
            }
        });
        logger.info("Processed callback payload successfully");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void handleTextMessageEvent(TextMessageEvent event) throws MessengerApiException, MessengerIOException {
        logger.info("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        logger.info("Received message'{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);
        int count =0;
        for (User user: this.users) {
            if (user.getId().equals(senderId)){
                if (messageText.toLowerCase().equals("stop")) user.setStatus(false);
                count = 0;
                break;
            } else {
                count++;
            }
        }
        if (count==this.users.size()) this.users.add(new User(senderId, true));
        sendTextMessage();
        logger.info("done 1");
    }
    @Scheduled(cron = "0 /5 * * *", zone = "Asia/Saigon")
    private void sendTextMessage() {
        for (User user:this.users) {
            if (user.isStatus()){
                try {
                    final IdRecipient recipient = IdRecipient.create(user.getId());
                    final NotificationType notificationType = NotificationType.REGULAR;
                    final String metadata = "DEVELOPER_DEFINED_METADATA";

                    final TextMessage textMessage = TextMessage.create("Hello. Enter \"stop\" to stop send message", empty(), of(metadata));
                    final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
                            of(notificationType), empty());
                    this.messenger.send(messagePayload);
                    logger.info("done");
                } catch (MessengerApiException | MessengerIOException e) {
                    handleSendException(e);
                }
            }
        }
    }

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }
}
