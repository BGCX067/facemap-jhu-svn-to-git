package edu.jhu.cs.oose.fall2011.facemap.domainImpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * A Person object contains all the account information visible to friends of
 * this person.
 * 
 * @author Orhan Ozguner
 *
 */

@Entity
@Table(name="PERSON")
public class PersonImpl implements Person {
	
	@Id
	@GeneratedValue
	@Column(name="PERSON_ID")
	private int personId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="PHONE_NUMBER")
	private String phoneNumber;
	
	@ManyToMany(targetEntity=FriendGroupImpl.class , mappedBy="friendsInGroup" , cascade=CascadeType.ALL ,fetch=FetchType.EAGER)
	@JoinTable(name = "Join_Person_FriendGroup", joinColumns = { @JoinColumn(name = "personId") }, inverseJoinColumns = { @JoinColumn(name = "groupId") })
	private Set<FriendGroup> group = new HashSet<FriendGroup>(); 
	

	public Set<FriendGroup> getGroup() {
		return group;
	}

	public void setGroup(Set<FriendGroup> group) {
		this.group = group;
	}

	public int getPersonId(){
		return personId;
	}
	
	public void setPersonId(int personId){
		this.personId = personId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	public LocationImpl getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}
