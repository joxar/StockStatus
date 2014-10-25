package utility;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class MailSender {

	public void sendMail(String userName, String passWord, String sendTo, String msgContent, String attachedFile) throws Exception {

		final String USR = userName;
		final String PW = passWord;
		final String DOMAIN = "@yahoo.co.jp";

		try {
			// configure for connecting with SMTP server
			Properties props = new Properties();
			props.setProperty( "mail.smtp.port", "587");
			props.setProperty("mail.smtp.socketFactory.port", "587");
			props.put( "mail.smtp.host", "smtp.mail.yahoo.co.jp" );
			props.setProperty("mail.smtp.auth", "true");
			Session session = Session.getInstance(props, new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(USR, PW);
				}
			});

			MimeMessage mimeMessage = new MimeMessage( session );

			// set attached files
//			Multipart mp = new MimeMultipart();
//			MimeBodyPart mbp = new MimeBodyPart();
//			FileDataSource fds = new FileDataSource(attachedFile);
//			mbp.setDataHandler(new DataHandler(fds));
//			mp.addBodyPart(mbp);

			// sendTo:
			mimeMessage.setFrom( new InternetAddress( USR+DOMAIN, USR, "iso-2022-jp" ) );
			mimeMessage.setRecipients( Message.RecipientType.TO, sendTo );
			// title:
			mimeMessage.setSubject( msgContent, "iso-2022-jp" );
			// content:
			mimeMessage.setText( "http://www.yodobashi.com/ec/support/apple/generaltop/index.html#yoyaku6", "iso-2022-jp" );
			// format:
			mimeMessage.setHeader( "Content-Type", "text/html" );
			// date:
			mimeMessage.setSentDate( new Date() );
			// attached file:
			// mimeMessage.setContent(mp);

			// execute sending
			Transport.send( mimeMessage );

		} catch (Exception e) {
			throw e;
		}

	}
}
