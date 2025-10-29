package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.repository.UserRepository;
import com.projetointegrador.comunicavet.utils.RandomNumberGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidCodeService validCodeService;

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("projeto-integrador@alwaysdata.net");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // 'true' indicates HTML content

        javaMailSender.send(message);
    }

    public Long sendValidRecoverCode(String to) throws MessagingException {
        int code = RandomNumberGenerator.generateSixDigitRandomNumber();

        // Verifica se este email existe no Banco de Dados
        Long userId = userRepository
                .findByEmail(to)
                .orElseThrow(() -> new NotFoundResourceException("Email incorreto"))
                .getId();

        // Registra este código no Banco de Dados
        validCodeService.create(code);

        this.sendEmail(to, "Code to change your password", "<h2>CODE<br>"+code+"<h2>");

        return userId;
    }
}
