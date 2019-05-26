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

	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html; charset=utf-8";
	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

	private static final String USERNAME = "c8b36f152fddae";
	private static final String GEHEIM = "823d4a128104d1";

	private static final String SMTP_HOST_PROPERTYNAME = "mail.smtp.host";
	private static final String SMTP_PORT_PROPERTYNAME = "mail.smtp.port";
	private static final String SMTP_AUTH_PROPERTYNAME = "mail.smtp.auth";
	private static final String SMTP_HOST = "smtp.mailtrap.io";
	private static final String SMTP_PORT = "2525";
	private static final String SMTP_AUTH = "true";

	private EmailSender(){}

	public static void sendEmail(String subject, String to, String messageBody, boolean asHtml) {

		Session session = createSession();

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("84854a3561-5e519e@inbox.mailtrap.io"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);

			if (asHtml) {
				message.setContent(messageBody, TEXT_HTML_CHARSET_UTF_8);
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

		Session session = createSession();

		try {

			for (int index = 0; index < toList.length; index++) {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("spammer@spammer.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toList[index]));
				message.setSubject(subject);

				if (asHtml) {
					message.setContent(messageBody, TEXT_HTML_CHARSET_UTF_8);
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

	private static Properties createProperties() {
		Properties properties = new Properties();
		properties.put(SMTP_HOST_PROPERTYNAME, SMTP_HOST);
		properties.put(SMTP_PORT_PROPERTYNAME, SMTP_PORT);
		properties.put(SMTP_AUTH_PROPERTYNAME, SMTP_AUTH);

		return properties;
	}

	private static Session createSession() {
		Properties props = createProperties();
		return Session.getInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(USERNAME, GEHEIM);
					}
				});
	}

}
