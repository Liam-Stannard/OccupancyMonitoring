package uk.ac.newcastle.stannard.liam;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Liam Stannard
 * 
 * name - A name for a person
 * primaryAddress - The primary address to be used by the occupancy monitoring algorithm
 * addresses - A list of the addresses mapped to this person
 *
 */
public class Person implements Serializable { 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5698226248243982630L;
	String name;
	String primaryAddress;
	ArrayList<String> addresses = new ArrayList<String>();

	/**
	 * @param name           - The persons name
	 * @param primaryAddress - The primary address used for that person
	 */
	public Person(String name, String primaryAddress) {
		this.name = name;
		setPrimaryAddress(primaryAddress);
	}

	/**
	 * @param name           - The persons name
	 * @param primaryAddress - The primary address used for that person
	 * @param addresses      - The list of other addresses used by the user, the
	 *                       element at 0 is the primary address
	 */
	public Person(String name, String primaryAddress, ArrayList<String> addresses) {
		this.name = name;
		setAddresses(addresses);
		if (!primaryAddress.equals("")) {
			setPrimaryAddress(primaryAddress);
		} else {
			setPrimaryAddress(addresses.get(0));
		}

	}

	/**
	 * Sets primaryAddress to address. Also updates the addresses list to have the
	 * primary address as element 0
	 * 
	 * @param address - The address to be set as the primary address
	 */
	public void setPrimaryAddress(String address) {
		if (!addresses.contains(address)) {
			addresses.add(0, address);
		} else {
			addresses.remove(address);
			addresses.add(0, address);
		}
		primaryAddress = address;
	}

	/**
	 * Sets this.addresses to addresses
	 * 
	 * @param addresses - A list of addresses to be set to this.addresses
	 */
	public void setAddresses(ArrayList<String> addresses) {
		for (String adr : addresses) {
			if (!this.addresses.contains(adr)) {
				this.addresses.add(adr);
			}
		}
	}

	/**
	 * removes the primary address
	 * 
	 * @param address - the address to be removed
	 */
	public void removePrimaryAddress(String address) {
		addresses.remove(address);
		setPrimaryAddress(addresses.get(0));
	}

	/**
	 * removes the address
	 * 
	 * @param address - the address to be removed
	 */
	public void removeAddress(String address) {

		if (addresses.indexOf(address) == 0) {
			addresses.remove(address);
			setPrimaryAddress(addresses.get(0));
		} else {
			addresses.remove(address);
		}
	}

	/**
	 * @return the field name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the field primaryAddress
	 */
	public String getPrimaryAddress() {
		return primaryAddress;
	}

	/**
	 * @return the field addresses
	 */
	public ArrayList<String> getAddresses() {
		return addresses;
	}

	/**
	 * toString prints out the person Object as its name.
	 */
	@Override
	public String toString() {
		String toReturnString = name;
		
		 
		 
		return toReturnString;
	}

}
