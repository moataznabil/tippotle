package net.doepner.mail;

import org.guppy4j.log.Log;
import org.guppy4j.log.LogProvider;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import static javax.mail.Message.RecipientType.TO;
import static org.guppy4j.log.Log.Level.error;
import static org.guppy4j.log.Log.Level.info;

/**
 * Sends simple emails by SMTP
 */
public final class SmtpEmailer implements Emailer {

    private final EmailConfig emailConfig;
    private final Log log;

    public SmtpEmailer(EmailConfig emailConfig, LogProvider logProvider) {
        this.emailConfig = emailConfig;
        log = logProvider.getLog(getClass());
    }

    @Override
    public void send(String recipient, String subject, String text) {

        final Session session = Session.getInstance(emailConfig.getProperties(), null);

        try {
            final MimeMessage msg = new MimeMessage(session);

            msg.setFrom(emailConfig.getSender());
            final String toAddress = emailConfig.getEmailAddress(recipient);
            msg.setRecipients(TO, toAddress);
            msg.setSubject(subject);
            msg.setText(text);

            Transport.send(msg, emailConfig.getUsername(), emailConfig.getPassword());
            log.as(info, "Sent email regarding '{}' to {}", subject, toAddress);

        } catch (MessagingException e) {
            log.as(error, "Sending email failed", e);
        }
    }
}
