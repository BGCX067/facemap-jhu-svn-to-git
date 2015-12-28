package edu.jhu.cs.oose.fall2011.facemap.entity;

import java.util.Date;
import java.util.Random;

import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.LocationImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Test LocationRetriever implementation that returns random locations that
 * deviate from a central location by a specified maximum amount
 * 
 * @author Daniel Cranford
 * 
 */
public class RandomLocationRetriever implements LocationRetriever {
	private final double centerLatitude;
	private final double centerLongitude;
	private final double latitudeDeviation;
	private final double longitudeDeviation;
	private final Random random;

	public RandomLocationRetriever(double centerLatitude,
			double centerLongitude, double latitudeDeviation,
			double longitudeDeviation) {
		this.centerLatitude = centerLatitude;
		this.centerLongitude = centerLongitude;
		this.latitudeDeviation = latitudeDeviation;
		this.longitudeDeviation = longitudeDeviation;
		this.random = new Random();
	}

	@Override
	public Location retrieveLocation() {
		return new LocationImpl(
				getRandom(centerLatitude, latitudeDeviation), 
				getRandom(centerLongitude, longitudeDeviation), 
				new Date());
	}

	private double getRandom(double center, double deviation) {
		return (random.nextDouble() - 0.5) * 2 * deviation + center;
	}

}
