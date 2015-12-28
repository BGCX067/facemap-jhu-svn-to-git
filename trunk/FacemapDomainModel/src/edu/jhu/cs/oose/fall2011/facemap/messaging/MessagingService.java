package edu.jhu.cs.oose.fall2011.facemap.messaging;

/**
 * Contains the three MessageSender implementations. One for SMS, one for email,
 * and one for in-application-IM.
 * 
 * @author Daniel Cranford
 *
 */
public class MessagingService {
	private MessageSender emailMessageSender = new EmailMessageSender();
	private MessageSender smsMessageSender = new SmsMessageSender();
	private MessageSender imMessageSender = new ImMessageSender();
	
	/**
	 * @return a MessageSender implementation that will send messages via email
	 */
	public MessageSender getEmailMessageSender() {
		return emailMessageSender;
	}
	
	/**
	 * @return a MessageSender implementation that will send messages via SMS 
	 *  (aka text)
	 */
	public MessageSender getSmsMessageSender() {
		return smsMessageSender;
	}
	
	/**
	 * @return a MessageSender implementation that will send messages via IM
	 */
	public MessageSender getImMessageSender() {
		return imMessageSender;
	}
}
