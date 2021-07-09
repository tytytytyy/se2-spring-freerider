package de.freerider.datamodel;

public class Customer {
	
	String id;
	String lastName;
	String firstName;
	String contact;
	Status status;

	public enum Status {
		  New,
		  InRegistration,
		  Active,
		  Suspended,
		  Deleted
		}

	public Customer(String firstName, String lastName,  String contact){
		this.id = null;
		this.lastName = lastName;
		this.firstName = firstName;
		this.contact = contact;
		this.status = Status.New;
		
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {

		if(this.status==Status.New || this.status == Status.Deleted) {
			this.id = id;
			this.setStatus(Status.InRegistration);
		}
		
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
