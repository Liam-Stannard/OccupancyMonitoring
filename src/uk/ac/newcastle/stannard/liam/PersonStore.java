package uk.ac.newcastle.stannard.liam;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonStore implements Serializable
{
	
	private ArrayList<Person> personList;
	
	
	public PersonStore()
	{
		personList = new ArrayList<Person>();
	}
	
	
	/**
	 * @param person
	 */
	public void addPerson(Person person)
	{
		personList.add(person);
	}
	
	/**
	 * @param person
	 */
	public void removePerson(Person person) 
	{
		personList.remove(person);
	}
	
	/**
	 * @return
	 */
	public ArrayList<Person> getPersonList()
	{
		return personList;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public Person getPerson(int index) 
	{
		return personList.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("People Stored:\n");
		for(Person p : personList)
		sBuilder.append(p);
		return sBuilder.toString();
	}

}
