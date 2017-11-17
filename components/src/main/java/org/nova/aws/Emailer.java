package org.nova.aws;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.nova.logging.Item;
import org.nova.logging.Logger;

public class Emailer
{
    final private String username;
    final private String password;
    final private String host;
    final private String from;
    final int port;
    final Session session; 
    final Logger logger;
    public Emailer(String from,String username,String password,String host,int port,Logger logger)
    {
        this.username=username;
        this.password=password;
        this.host=host;
        this.port=port;
        this.from=from;
        this.logger=logger;
        
        Properties props=System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.port", port); 
        
        // Set properties indicating that we want to use STARTTLS to encrypt the connection.
        // The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
    
        // Create a Session object to represent a mail session with the specified properties. 
        this.session = Session.getDefaultInstance(props);
    }
    public void send(String to,String subject,String content,String mediaType) throws Throwable
    {
        send(this.from,to,subject,content,mediaType);
    }    
    public void send(String from,String to,String subject,String content,String mediaType) throws Throwable
    {
        // Create a message with the specified information. 

        MimeMessage msg = new MimeMessage(this.session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(content,mediaType);
            
        // Create a transport.        
        Transport transport = this.session.getTransport();
        // Send the message.
        try
        {
//            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(this.host, this.username, this.password);
            
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
//            System.out.println("Email sent!");
            this.logger.log("Emailer",new Item("from",from),new Item("to",to),new Item("subject",subject),new Item("mediaType",mediaType),new Item("content",content));
        }
        /*
        catch (Exception ex) 
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        */
        finally
        {
            // Close and terminate the connection.

            transport.close();
        }
        
    }
    
}
