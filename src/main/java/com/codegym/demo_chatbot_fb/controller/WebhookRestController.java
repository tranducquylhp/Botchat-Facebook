package com.codegym.demo_chatbot_fb.controller;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;

import com.codegym.demo_chatbot_fb.model.CodeExercise;
import com.codegym.demo_chatbot_fb.model.User;
import com.codegym.demo_chatbot_fb.service.CodeExerciseService;
import com.codegym.demo_chatbot_fb.service.UserService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RestController
public class WebhookRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private CodeExerciseService codeExerciseService;

    @Value("${message-notText}")
    String messageNotText;

    private static final Logger logger = LoggerFactory.getLogger(WebhookRestController.class);

    private final Messenger messenger;

    @Autowired
    public WebhookRestController(final Messenger messenger) {
        this.messenger = messenger;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.challenge") final String challenge) {
        return ResponseEntity.ok(challenge);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerVerificationException {
        logger.info("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        this.messenger.onReceiveEvents(payload, of(signature), event -> {
            if (event.isTextMessageEvent()) {
                try {
                    logger.info("0");
                    handleTextMessageEvent(event.asTextMessageEvent());
                    logger.info("1");
                } catch (MessengerApiException e) {
                    logger.info("2");
                    e.printStackTrace();
                } catch (MessengerIOException e) {
                    logger.info("3");
                    e.printStackTrace();
                }
            } else {
                String senderId = event.senderId();
                sendTextMessageUser(senderId, messageNotText);
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
        logger.info("user");
        if (userService.findById(senderId).isPresent()){
            Optional<User> user = userService.findById(senderId);
            if (messageText.toLowerCase().equals("stop")) {
                sendTextMessageUser(senderId, "Hello. You have ended receiving scheduled messages");
                user.get().setStatus(false);
                logger.info("abc");
            } else {
                user.get().setStatus(true);
                sendTextMessageUser(senderId, "Hello. You have started receiving scheduled messages");
                logger.info("abcd");
            }
            userService.save(user.get());
        } else {
            logger.info("save user");
            User user = new User(senderId,true);
            userService.save(user);
            logger.info("saved");
            sendTextMessageUser(senderId, "Welcome! You have started receiving scheduled messages");
        }
    }

    private void sendTextMessageUser(String idSender, String text) {
        try {
            final IdRecipient recipient = IdRecipient.create(idSender);
            final NotificationType notificationType = NotificationType.REGULAR;
            final String metadata = "DEVELOPER_DEFINED_METADATA";

            final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
            final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
                    of(notificationType), empty());
            this.messenger.send(messagePayload);
            logger.info("done");
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Saigon")
    private void sendTextMessage() {
        logger.info("qwert");
        ArrayList<User> users = (ArrayList<User>) userService.findAllByStatusIsTrue();
        CodeExercise codeExercise = codeExerciseService.findCodeExerciseTrueFirst();
        if (codeExercise != null) {
            for (int i = 0; i < users.size(); i++) {
                sendTextMessageUser(users.get(i).getId(),
                        LocalDate.now() + "\n" + codeExercise.getTitle() + "\n" + codeExercise.getContent());
            }
            codeExercise.setStatus(false);
            codeExerciseService.save(codeExercise);
        } else {
            for (int i = 0; i < users.size(); i++) {
                sendTextMessageUser(users.get(i).getId(),"Currently running out of homework, waiting for the admin to update the new lesson.");
            }
        }
        logger.info("qwerty");
    }

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }
}
