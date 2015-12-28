package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;
import java.util.Date;


public class LocationImpl implements Location, Serializable {

	private static final long serialVersionUID = 1L;
	double latitude;
	double longitude;
	Date timestamp;
	
	public LocationImpl(double latitude, double longitude, Date timestamp) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationImpl other = (LocationImpl) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocationImpl [latitude=" + latitude + ", longitude="
				+ longitude + ", timestamp=" + timestamp + "]";
	}
	
	
}
