package edu.jhu.cs.oose.fall2011.facemap.domainImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.Location;

/**
 * This class contains a set of GPS coordinates.
 * 
 * @author Orhan Ozguner
 *
 */

@Entity
@Table(name="LOCATION")
public class LocationImpl implements Location {
	
	@Id
	@GeneratedValue
	@Column(name="LOCATION_ID")
	private int locationId;
	
	@Column(name="LATITUDE")
	private double latitude;
	
	@Column(name="LONGITUDE")
	private double longitude;
	
	
	public int getId(){
		return locationId;
	}
	
	public void setId(int locationId){
		this.locationId = locationId;
	}
	
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	

	public double getLongitude() {
		return longitude;
	}
	

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
