package com.cardealer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailCont {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendOrderConfirmationEmail(String to) {
        String subject = "Подтверждение заказа";
        String text = "Уважаемый клиент,\n\n" +
                "Спасибо за покупку автомобиля!\n\n" +
                "В качестве благодарности за ваш заказ, мы рады предложить вам скидку 5% на следующую покупку автомобиля.\n\n" +
                "С уважением,\n" +
                "Команда Интернет-магазина";
        sendEmail(to, subject, text);
    }



}
