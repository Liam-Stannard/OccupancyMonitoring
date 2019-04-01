package uk.ac.newcastle.stannard.liam;

import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		Interface i = new Interface();
		
		
		ArrayList<String> liam_addresseStrings = new ArrayList<String>();
		liam_addresseStrings.add("d8:9c:67:7b:75:71");
		liam_addresseStrings.add("3c:f7:a4:48:8a:09");
		i.getPersonStore().addPerson(new Person("Liam Stannard","14:c2:13:96:f1:f5",liam_addresseStrings));
		
		
		ArrayList<String> paul_addresseStrings = new ArrayList<String>();
		paul_addresseStrings.add("3c:f7:a4:a9:b3:65");
	    i.getPersonStore().getPersonList().add(new Person("Paul Stannard","cc:9f:7a:9c:d0:1e",paul_addresseStrings));
	    
	    ArrayList<String> pauline_addresseStrings = new ArrayList<String>();
		pauline_addresseStrings.add("9c:e0:63:14:c6:42");
	    i.getPersonStore().getPersonList().add(new Person("Pauline Stannard","9c:e0:63:14:c6:42",pauline_addresseStrings));
		
	}

}
