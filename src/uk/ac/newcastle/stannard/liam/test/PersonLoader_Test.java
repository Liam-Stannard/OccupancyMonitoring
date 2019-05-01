/**
 * 
 */
package uk.ac.newcastle.stannard.liam.test;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.NEW;

import uk.ac.newcastle.stannard.liam.ObjectSerialiser;
import uk.ac.newcastle.stannard.liam.Person;
import uk.ac.newcastle.stannard.liam.PersonStore;

/**
 * @author wwwli
 *
 */
public class PersonLoader_Test {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
	ObjectSerialiser<PersonStore> personLoader = new ObjectSerialiser<PersonStore>("res/personStoreTest");
	PersonStore personStore = new PersonStore();
	Person p1 = new Person("A", "2");
	Person p2 = new Person("B", "13");
	personStore.addPerson(p1);
	personStore.addPerson(p2);
	
	personLoader.save(personStore);
	
	PersonStore loaded = personLoader.load();
	System.out.println("-----------------------------");
	System.out.println("- Person Store -");
	System.out.println(personStore);
	System.out.println("-----------------------------");
	System.out.println("- Loaded -");
	System.out.println(loaded);
	
	System.out.println(loaded.equals(personStore));

	}

	

}
