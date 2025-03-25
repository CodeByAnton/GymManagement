package com.annton.api.services;

import com.annton.api.services.enums.TemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    @Getter
    private String senderEmail;


    public void sendConfirmationEmail(String to,
                                      String activationCode
    ) throws MessagingException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("activation_code", activationCode);

        prepareAndSendEmail(properties, TemplateName.CONFIRMATION_EMAIL, to);
    }

    @Async
    public void sendPassCodeEmail(String to,
                                  String passCode
    ) throws MessagingException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("pass_code", passCode);

        prepareAndSendEmail(properties, TemplateName.PASS_CODE_EMAIL, to);
    }

    @Async
    public void prepareAndSendEmail(Map<String, Object> properties, TemplateName templateName, String to) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                UTF_8.name());

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom(senderEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(templateName.getSubject());

        String template
                = templateEngine.process(templateName.getName(), context);

        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);

        CompletableFuture.completedFuture(null);
    }

}
