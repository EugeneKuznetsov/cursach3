package com.cardealer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailRegCont {

    private static final Logger logger = LoggerFactory.getLogger(EmailRegCont.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

        logger.info("Email sent to: {}", to);
    }

    public void sendRegistrationConfirmationEmail(String to) {
        String subject = "Спасибо за регистрацию!";
        String text = "Уважаемый клиент,\n\n" +
                "Спасибо за регистрацию в нашем интернет-магазине автомобилей!\n\n" +
                "Мы ценим ваш выбор и готовы предложить вам дополнительный год гарантии на ваш будущий автомобиль при его заказе в течение недели.\n\n" +
                "С уважением,\n" +
                "Команда Интернет-магазина";
        sendEmail(to, subject, text);

        logger.info("Registration confirmation email sent to: {}", to);
    }

}
