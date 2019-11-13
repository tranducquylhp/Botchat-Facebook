package com.codegym.demo_chatbot_fb;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("com.codegym.demo_chatbot_fb.repository")
@EntityScan("com.codegym.demo_chatbot_fb.model")
public class DemoChatbotFbApplication {

    @Bean
    public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
                               @Value("${messenger4j.appSecret}") final String appSecret,
                               @Value("${messenger4j.verifyToken}") final String verifyToken) {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoChatbotFbApplication.class, args);
    }

}
