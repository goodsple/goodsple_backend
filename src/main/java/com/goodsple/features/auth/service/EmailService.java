package com.goodsple.features.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // application.yml의 email.verification.from-address 값을 주입
    @Value("${email.verification.from-address}")
    private String fromAddress;

    // 인증 이메일 전송 메서드
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        try {
        // 1) Thymeleaf 컨텍스트 세팅
        Context ctx = new Context();
        ctx.setVariable("code", verificationCode);

        // 2) HTML 렌더링
        String htmlContent = templateEngine.process("verification-email", ctx);

        // 3) 메일 생성 & 전송
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        // 발신자 이메일 설정
        helper.setFrom(fromAddress);  // 발신자 이메일 (환경변수 또는 고정 값)
        // 수신자 이메일 설정
        helper.setTo(toEmail);  // 수신자 이메일 (예: 사용자가 입력한 이메일)
        // 이메일 제목
        helper.setSubject("🎉 Goodsple 이메일 인증 코드");
        // 이메일 본문
        helper.setText(htmlContent, true);  // HTML 모드
        // 메일 전송
        mailSender.send(message);  // 이메일 전송
        } catch (MessagingException e) {
            // 로그를 남기고, 원하는 방식으로 감싸 던집니다.
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }
}
