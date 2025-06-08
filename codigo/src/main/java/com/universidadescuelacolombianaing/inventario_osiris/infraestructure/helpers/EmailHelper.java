package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers;

import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
@Slf4j
public class EmailHelper {
	@Value("${spring.mail.username}")
	private String from;

	private final JavaMailSender mailSender;
	private final Environment environment;

	public EmailHelper(JavaMailSender mailSender, Environment environment) {
		this.mailSender = mailSender;
		this.environment = environment;
	}

	public void sendEmail(String to, String subject, String body) {
		MimeMessage message = mailSender
			.createMimeMessage();

		String htmlContent = this.readHtmlTemplate(body);

		try {
			message.setFrom(new InternetAddress(from));
			message.setHeader("Content-Type", "text/html; charset=UTF-8");
			message.setSubject(subject + " | INVENTARIO OSIRIS");
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setContent(htmlContent, "text/html; charset=UTF-8");
			mailSender.send(message);
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			log.error("Error to send email. " + e.getMessage());
		}
	}

	private String readHtmlTemplate(String body) {
		String htmlFile = "email_template.html";

		Path TEMPLATE_PATH = Paths.get(Objects.requireNonNull(
                	environment.getProperty("route.email.html")) + htmlFile);

		try (var lines = Files.lines(TEMPLATE_PATH)) {
			String html = lines.collect(Collectors.joining());
			return html.replace("{fecha}", LocalDate.now().toString().replaceAll("-", "/")).replace("{body}", body);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			log.error("Cant read html template. " + e.getMessage());
			throw new ServerErrorException();
		}
	}
}
