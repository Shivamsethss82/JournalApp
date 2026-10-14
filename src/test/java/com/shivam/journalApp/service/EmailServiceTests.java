package com.shivam.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail() {
        emailService.sendEmail("shivamseth8682@gmail.com", "Testing Java Mail Service",
                "Testing Java Mail AAP KAISE HAI ??");
    }
}
