package uk.ac.newcastle.stannard.liam;

import java.util.ArrayList;

public class PersonStore 
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

}
