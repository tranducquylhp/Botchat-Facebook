package com.codegym.demo_chatbot_fb.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.github.messenger4j.Messenger.*;

@RestController
public class WebhookRestController {
    private static final String RESOURCE_URL = "https://raw.githubusercontent.com/fbsamples/messenger-platform-samples/master/node/public";

    private static final Logger logger = LoggerFactory.getLogger(WebhookRestController.class);

    private final Messenger messenger;

    @Autowired
    public WebhookRestController(final Messenger messenger) {
        this.messenger = messenger;
    }



    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> responseMessage(@RequestBody LinkedHashMap object) {
        ArrayList<Object> entry = (ArrayList<Object>) object.get("entry");
        LinkedHashMap entries = (LinkedHashMap) entry.get(0);

        ArrayList<Object> messaging = (ArrayList<Object>) entries.get("messaging");

        LinkedHashMap messagings = (LinkedHashMap) messaging.get(0);
        LinkedHashMap sender = (LinkedHashMap) messagings.get("sender");

        String senderId = (String) sender.get("id");

        LinkedHashMap message = (LinkedHashMap) messagings.get("message");

        String text = (String) message.get("text");
        String messageResponse = "";
        if (text.contains("hello")) {
            messageResponse = "hi";
        } else messageResponse = "I'm bot";

        String response = "{\n" +
                "    \"recipient\":{\n" +
                "        \"id\":\"" + senderId + "\"\n" +
                "    }, \n" +
                "    \"message\":{\n" +
                "        \"text\":\"" + messageResponse + "\"\n" +
                "    }\n" +
                "}";
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
