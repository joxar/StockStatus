package utility;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {

	public static void main(String[] args) {
		try {
			// SMTPサーバー設定
			Properties props = System.getProperties();
			props.setProperty( "mail.smtp.port", "587");
			props.setProperty("mail.smtp.socketFactory.port", "587");
			props.put( "mail.smtp.host", "smtp.mail.yahoo.co.jp" );
			props.setProperty("mail.smtp.auth", "true");
			Session session = Session.getInstance(props, new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication( "mfpirlo", "B@10Na" );
				}
			});

			MimeMessage mimeMessage=new MimeMessage( session );

			// 送信元メールアドレスと送信者名を指定
			mimeMessage.setFrom( new InternetAddress( "mfpirlo@yahoo.co.jp", "送信者名", "iso-2022-jp" ) );
			// 送信先メールアドレスを指定
			mimeMessage.setRecipients( Message.RecipientType.TO, "mfpirlo@yahoo.co.jp" );
			// メールのタイトルを指定
			mimeMessage.setSubject( "Hello World JavaMail", "iso-2022-jp" );
			// メールの内容を指定
			mimeMessage.setText( "<h1>Hello World JavaMail</h1>", "iso-2022-jp" );
			// メールの形式を指定
			mimeMessage.setHeader( "Content-Type", "text/html" );
			// 送信日付を指定
			mimeMessage.setSentDate( new Date() );
			// 送信します
			Transport.send( mimeMessage );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}