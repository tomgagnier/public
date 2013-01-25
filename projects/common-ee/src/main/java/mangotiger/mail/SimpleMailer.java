package mangotiger.mail;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * This simple mailer always sends mail to the JIRA web_support account.  It does not support attachments.
 *
 * @author tom_gagnier@yahoo.com
 */
public class SimpleMailer {
  private JavaMailSenderImpl sender;

  /**
   * The default web suppport mailer, debug is turned off.
   *
   * @param host     the mail host
   * @param user     the mail host user account
   * @param password the mail host user account password
   */
  public SimpleMailer(final String host, final String user, final String password) {
    this(host, user, password, false);
  }

  /**
   * Use this constructor to turn debug mode on.
   *
   * @param host     the mail host
   * @param user     the mail host user account
   * @param password the mail host user account password
   * @param debug    if true, Java Mail will print diagnostics to standard out.
   */
  public SimpleMailer(final String host, final String user, final String password, final boolean debug) {
    final Properties properties = new Properties();
    properties.setProperty("mail.smtp.auth", Boolean.toString(debug));
    properties.setProperty("mail.debug", Boolean.toString(debug));
    sender = new JavaMailSenderImpl();
    sender.setHost(host);
    sender.setUsername(user);
    sender.setPassword(password);
    sender.setJavaMailProperties(properties);
  }

  /**
   * Send a mail message.
   *
   * @param from    the from field
   * @param to      the to field
   * @param subject the subject field
   * @param text    the mail text
   * @throws MessagingException
   */
  public void send(final String from, final String to, final String subject, final String text) throws MessagingException {
    final Letter letter = new Letter(from, to, subject, text);
    log().info(letter); // TODO: change this to debug()
    final MimeMessage message = sender.createMimeMessage();
    final MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(from.trim());
    helper.setTo(to.trim());
    helper.setText(text.trim());
    helper.setSubject(subject.trim());
    sender.send(message);
  }

  private static Log log() {
    return LogFactory.getLog(SimpleMailer.class);
  }

  private static class Letter {
    private final String from;
    private final String to;
    private final String subject;
    private final String text;

    public Letter(String from, String to, String subject, String text) {
      this.from = from;
      this.to = to;
      this.subject = subject;
      this.text = text;
    }

    public String toString() {
      return "LogEntry" +
          "{from='" + from + "'" +
          ",to='" + to + "'" +
          ",subject='" + subject + "'" +
          ",text='" + text.substring(0, 10) + "...'" +
          "}";
    }
  }

  /**
   * A simple program to test the mailer, since unit testing is not feasible.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    try {
      final String user = "support@mangotiger.com";
      final String password = "web_support";
      final String host = "mail.mangotiger.com";
      final SimpleMailer simpleMailer = new SimpleMailer(host, user, password, true);
      final String from = "tom_gagnier@yahoo.com";
      simpleMailer.send(from, user, "subject", "text");
    } catch (MessagingException e) {
      log().error(e, e);
    }
  }
}
