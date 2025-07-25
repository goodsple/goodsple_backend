package com.goodsple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// 설정 클래스 역할
@Configuration
public class MailConfig {
    // Spring Boot 애플리케이션에서 이메일 전송을 위한 SMTP 서버 설정을 제공하며,
    // 이메일을 발송할 때 사용하는 JavaMailSender 객체를 Spring IoC 컨테이너에 등록

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    // 이메일을 발송하기 위한 설정을 하나의 빈(bean)으로 등록하는 역할
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();  // SMTP 서버와 연결하기 위한 설정
            mailSender.setHost(host); // SMTP 서버 주소
            mailSender.setPort(port);  // 포트(Gmail은 587번 포트 사용)
            mailSender.setUsername(username); // 이메일 계정
            mailSender.setPassword(password);  // 이메일 비밀번호

        // 추가 설정: STARTTLS 활성화
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");       // TLS 사용
        props.put("mail.smtp.starttls.required", "true");     // TLS 필수
        props.put("mail.debug", "true");                     // 필요시 true로 디버그 로그

        return mailSender; // JavaMailSender 객체를 반환하여 Spring에서 관리할 수 있게 함

    }
}
