package pl.kurs.java.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pl.kurs.java.model.entity.Token;
import pl.kurs.java.model.entity.User;

@Service
@AllArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	@Async
	public void sendEmail(User email, Token token) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("testermysql@gmail.com");
		msg.setFrom("Aleksander@Kulbat.pl");
		msg.setSubject("Potwierdzenie");
		msg.setText(
				"http://localhost:8080/registration/verify?token=" + token.getUuid() + "&email=" + email.getEmail());
		javaMailSender.send(msg);
		System.out.println("Koncze wysyłanie potwierdzenia");

	}

}
