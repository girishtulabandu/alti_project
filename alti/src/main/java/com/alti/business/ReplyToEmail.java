package com.alti.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReplyToEmail {
	public static void replyMail(String string) {
		Date date = null;

		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(
				"C:\\Users\\ltulabandu\\workspace\\myproject\\properties\\smtpConfig.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Session session = Session.getInstance(properties);

		// session.setDebug(true);
		try {
			// Get a Store object and connect to the current host
			Store store = session.getStore("pop3s");
			store.connect("pop.gmail.com", "girishfreak@gmail.com", "Girish@341.");//change it accordingly

			Folder folder = store.getFolder("inbox");
			if (!folder.exists()) {
				System.out.println("inbox not found");
				System.exit(0);
			}
			folder.open(Folder.READ_ONLY);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			Message[] messages = folder.getMessages();
			if (messages.length != 0) {

				for (int i = 0, n = messages.length; i < n; i++) {
					Message message = messages[i];
					date = message.getSentDate();
					// Get all the information from the message
					String from = InternetAddress.toString(message.getFrom());
					if (from != null) {
						System.out.println("From: " + from);
					}
					String replyTo = InternetAddress.toString(message.getReplyTo());
					if (replyTo != null) {
						System.out.println("Reply-to: " + replyTo);
					}
					String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
					if (to != null) {
						System.out.println("To: " + to);
					}

					String subject = message.getSubject();
					if (subject != null) {
						System.out.println("Subject: " + subject);
					}
					Date sent = message.getSentDate();
					if (sent != null) {
						System.out.println("Sent: " + sent);
					}

					System.out.print("Do you want to reply [y/n] : ");
					String ans = reader.readLine();
					if ("Y".equals(ans) || "y".equals(ans)) {

						Message replyMessage = new MimeMessage(session);
						replyMessage = (MimeMessage) message.reply(false);
						replyMessage.setFrom(new InternetAddress(to));
						replyMessage.setText(string);
						replyMessage.setReplyTo(message.getReplyTo());

						// Send the message by authenticating the SMTP server
						// Create a Transport instance and call the sendMessage
						Transport t = session.getTransport("smtp");
						try {
							// connect to the smtp server using transport
							// instance
							// change the user and password accordingly
							t.connect(properties.getProperty("userName"), properties.getProperty("password"));
							t.sendMessage(replyMessage, replyMessage.getAllRecipients());
						} finally {
							t.close();
						}
						System.out.println("message replied successfully ....");

						// close the store and folder objects
						folder.close(false);
						store.close();

					} else if ("n".equals(ans)) {
						break;
					}
				} // end of for loop

			} else {
				System.out.println("There is no message.");
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void reply() {
		Connection conn = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> unapprovedList = new ArrayList<String>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tails", "root", "root");
			pStatement = conn.prepareStatement("select * from details where status = 'Yes'");
			rs = pStatement.executeQuery();
			while (rs.next()) {
				String num = rs.getString("InvoiceNo");
				list.add(num);

			}
			String listString = "";
			for (String i : list) {
				listString += i + " ";
			}
			
		
			replyMail("The following invoice numbers/number " + "\n" + listString + " has been approved successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
