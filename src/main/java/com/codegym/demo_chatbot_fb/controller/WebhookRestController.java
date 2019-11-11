package com.codegym.demo_chatbot_fb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
public class WebhookRestController {
    @Value("${verifyToken}")
    String VERYFY_TOKEN;


    @GetMapping("/webhook")
    public ResponseEntity<String> veryfyWebhook(HttpServletRequest request, HttpServletResponse response) {
        String mode = request.getParameter("hub.mode");
        String token = request.getParameter("hub.verify_token");
        String challenge = request.getParameter("hub.challenge");


        // Checks the mode and token sent is correct
        if (mode != null && token != null) {
            if (mode == "subscribe" && token == VERYFY_TOKEN) {

                // Responds with the challenge token from the request
                return new ResponseEntity<String>(challenge, HttpStatus.OK);

            } else {
                // Responds with '403 Forbidden' if verify tokens do not match
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
