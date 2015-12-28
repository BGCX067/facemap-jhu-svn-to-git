package edu.jhu.cs.oose.fall2011.facemap.gui;

/**
 * The Enum ActionType for what we do with contacts
 * These parameters are used when the user is in the group page and click the corresponding button, 
 * the ActionType variable is passed to the next page, so the corresponding friends can fetched be
 * fetched from the model
 * @author Ying Dou
 */
public enum ActionType {
	
	/** add members to group */
	ADD,
	
	/** delete members in a group */
	DELETE,
	
	/** block members in a group */
	BLOCK,
	
	/** unblock members in a group */
	UNBLOCK,
	
	/**
	 * remove members from a group and the contacts are still kept
	 */
	REMOVE
}
