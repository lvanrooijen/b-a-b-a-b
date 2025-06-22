package com.lvr.babab.babab.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class MailConfiguration {
  @Bean
  public JavaMailSender getMailSender() {
    return new JavaMailSenderImpl();
  }

  @Bean
  public TemplateEngine getTemplateEngine() {
    return new TemplateEngine();
  }
}
