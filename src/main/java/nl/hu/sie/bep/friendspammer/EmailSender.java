package nl.hu.sie.bep.friendspammer;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSender {

	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

	private EmailSender(){}

	public static void sendEmail(String subject, String to, String messageBody, boolean asHtml) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.mailtrap.io");
		props.put("mail.smtp.port", "2525");
		props.put("mail.smtp.auth", "true");

		String username = "c8b36f152fddae";
		String geheim = "823d4a128104d1";

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, geheim);
					}
				});
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("84854a3561-5e519e@inbox.mailtrap.io"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);

			if (asHtml) {
				message.setContent(messageBody, "text/html; charset=utf-8");
			} else {
				message.setText(messageBody);
			}
			Transport.send(message);

			MongoSaver.saveEmail(to, "spammer@spamer.com", subject, messageBody, asHtml);

		} catch (MessagingException e) {
			logger.error("Error send e-mail.", e);
		}
	}

	public static void sendEmail(String subject, String[] toList, String messageBody, boolean asHtml) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.mailtrap.io");
		props.put("mail.smtp.port", "2525");
		props.put("mail.smtp.auth", "true");

		String username = "c8b36f152fddae";
		String geheim = "823d4a128104d1";

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, geheim);
					}
				});
		try {

			for (int index = 0; index < toList.length; index++) {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("spammer@spammer.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toList[index]));
				message.setSubject(subject);

				if (asHtml) {
					message.setContent(messageBody, "text/html; charset=utf-8");
				} else {
					message.setText(messageBody);
				}
				Transport.send(message);

				logger.info("Managed to send the message.");
			}

		} catch (MessagingException e) {
			logger.error("Error send e-mail.", e);
		}
	}

}
