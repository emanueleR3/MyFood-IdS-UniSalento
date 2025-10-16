package it.unisalento.myfood.Business;


import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    private static MailSender instance;

    private static final String username = "myfood.unisalento@gmail.com";
    private static final String password = "bnqz bahq dfka wkjx";   // application password

    public static synchronized MailSender getInstance() {
        if(instance == null)
            instance = new MailSender();
        return instance;
    }

    public void sendOnlyText(String destinatario, String oggetto, String messaggio) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            }
        );

        try {
            // Messaggio
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(oggetto);
            message.setText(messaggio);

            //send message
            Transport.send(message);
            System.out.println("Email inviata con successo!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("ERRORE! Email non inviata!");
        }
    }

    public void sendWithAttachment(String destinatario, String oggetto, String messaggio, File allegato) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(oggetto);

            // Creazione del corpo del messaggio
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messaggio);

            // Creazione dell'allegato
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(allegato.getPath());
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(allegato.getName());

            // Creazione della parte multipart per il messaggio
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            // Aggiunta delle parti al messaggio
            message.setContent(multipart);

            // Invio del messaggio
            Transport.send(message);

            System.out.println("Email con allegato inviata con successo!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("ERRORE! Email non inviata!");
        }
    }
}
