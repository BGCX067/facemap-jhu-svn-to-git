package edu.jhu.cs.oose.fall2011.facemap.server;

import edu.jhu.cs.oose.fall2011.facemap.domain.Location;

/**
 * Retrieves a Location instance from whatever source
 * @author Daniel Cranford
 */
public interface LocationRetriever {
	/**
	 * Returns the location this retriever is set up to retrieve
	 * @return the location this retriever is set up to retrieve
	 */
	Location retrieveLocation();
}
