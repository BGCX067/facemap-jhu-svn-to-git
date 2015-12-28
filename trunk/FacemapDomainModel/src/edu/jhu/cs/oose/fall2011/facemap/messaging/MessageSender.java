package edu.jhu.cs.oose.fall2011.facemap.messaging;

import java.util.List;

import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Provides a common interface to send a message.
 * @author Daniel Cranford
 *
 */
public interface MessageSender {
	/**
	 * Sends a String message to a list of recipients
	 * @param message text of message to send
	 * @param recipients list of recipients
	 */
	public void sendMessage(String message, List<Person> recipients);
}
