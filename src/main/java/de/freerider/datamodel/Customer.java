package de.freerider.datamodel;


public class Customer {
	//
	private String id = null;
	//
	private String lastName;
	//
	private String firstName;
	//
	private String contact;
	//
	public enum Status { New, InRegistration, Active, Suspended, Deleted };
	private Status status = Status.New;


	/**
	 * Public constructor.
	 * 
	 * @param lastName value of lastName attribute, null allowed -> ""
	 * @param firstName value of firstName attribute, null allowed -> ""
	 * @param contact value of contact attribute, null allowed -> ""
	 */
	public Customer( String lastName, String firstName, String contact ) {
		setLastName( lastName );
		setFirstName( firstName );
		setContact( contact );
	}


	public String getId() {
		return id;
	}

	/**
	 * Set id attribute only if current id is null or reset id to null.
	 * Does not alter non-null id values.
	 * 
	 * @param id to set; null resets id, non-null id only set when this.id == null
	 */
	public void setId( final String id ) {
		if( this.id == null || id == null ) {	// set id only once or reset to null
			this.id = id;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName == null? "" : lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName == null? "" : firstName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact( String contact ) {
		this.contact = contact == null? "" : contact;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus( Status status ) {
		this.status = status;
	}
}
